package com.sky.service.impl;

import com.sky.annotation.LogAnnotation;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;

    @LogAnnotation(value = "用户提交订单")
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //从ThreadLocal中获取当前操作用户
        Long id = BaseContext.getCurrentId();
        //1. 检测异常（用户购物车空异常、地址信息空异常）
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getByUserId(id);
        if(CollectionUtils.isEmpty(shoppingCartList)){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //2. 将订单信息存入数据库
        Orders orders = new Orders();
        //借助BeanUtils直接赋值
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        //为其余信息赋值
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        User user = userMapper.getById(id);
        orders.setUserName(user.getName());
        orders.setPhone(addressBook.getPhone());
        String address = addressBook.getProvinceName()+
                addressBook.getCityName()+
                addressBook.getDistrictName()+
                addressBook.getDetail();
        orders.setAddress(address);
        orders.setConsignee(addressBook.getConsignee());
        orderMapper.insertDynamic(orders);
        Long orderId = orders.getId();
        //3. 将订单详情信息存入数据库
        //整理订单信息
        List<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCartList.forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetailList);
        //4. 封装返回结果
//        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
//        orderSubmitVO.setId(orderId);
//        orderSubmitVO.setOrderNumber(orders.getNumber());
//        orderSubmitVO.setOrderAmount(ordersSubmitDTO.getAmount());
//        orderSubmitVO.setOrderTime(orders.getOrderTime());
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orderId)
                .orderNumber(orders.getNumber())
                .orderAmount(ordersSubmitDTO.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        //5. 删除购物车中的数据
        shoppingCartMapper.deleteByUserId(id);
        return orderSubmitVO;
    }
}
