package com.driver.service.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.driver.shared.dto.OrderDto;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.io.entity.OrderEntity;
import com.driver.io.repository.OrderRepository;
import com.driver.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	/**
	 * Logger initialization
	 */
	org.slf4j.Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * Create order method
	 */
	@Override
	public OrderDto createOrder(OrderDto order) {

		logger.info("OrderService -> createOrder(order) method is called.");

		ModelMapper modelMapper = new ModelMapper();
		OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);

		logger.info("Creating new order");

		String orderId = String.valueOf(new SecureRandom());
		orderEntity.setOrderId(orderId);
		orderEntity.setStatus(false);

		logger.info("Saving the order to the database -> orders");

		OrderEntity storedOrder = orderRepository.save(orderEntity);
		OrderDto returnValue = modelMapper.map(storedOrder, OrderDto.class);

		logger.info("Returning the order details to the OrderController via OrderDto object");

		return returnValue;
	}

	/**
	 * Get order by order id method
	 */
	@Override
	public OrderDto getOrderById(String orderId) throws Exception {

		logger.info("OrderService -> getOrderById(orderId) method is called. Finding the order");

		OrderDto returnValue = new OrderDto();
		ModelMapper modelMapper = new ModelMapper();

		OrderEntity orderEntity = orderRepository.findByOrderId(orderId);

		if (orderEntity == null) {

			logger.error("Can't find the order id from the database");

			throw new Exception(orderId);
		}

		returnValue = modelMapper.map(orderEntity, OrderDto.class);

		logger.info("Order details found. Returning the order to the OrderController via OrderDto object");

		return returnValue;
	}

	/**
	 * Update food method
	 */
	@Override
	public OrderDto updateOrderDetails(String orderId, OrderDto order) throws Exception {

		logger.info("OrderService -> updateOrderDetails(orderId, order) method is called. Finding the order");

		OrderDto returnValue = new OrderDto();
		ModelMapper modelMapper = new ModelMapper();

		OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
		if (orderEntity == null) {

			logger.error("Can't find the order id from the database");

			throw new Exception(orderId);
		}

		logger.info("Order details found. Updating order details");

		orderEntity.setCost(order.getCost());
		orderEntity.setItems(order.getItems());
		orderEntity.setStatus(true);

		logger.info("Saving updated details to the database -> orders");

		OrderEntity updatedOrder = orderRepository.save(orderEntity);
		returnValue = modelMapper.map(updatedOrder, OrderDto.class);

		logger.info("Returning the order to the OrderController via OrderDto object");

		return returnValue;
	}

	@Override
	public void deleteOrder(String orderId) throws Exception{

		logger.info("OrderService -> deleteOrder(orderId) method is called. Finding the order");

		OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
		if (orderEntity == null) {

			logger.error("Can't find the order id from the database");

			throw new Exception(orderId);
		}

		orderRepository.delete(orderEntity);

	}

	/**
	 * Delete use method
	 */
	@Override
	public List<OrderDto> getOrders() {

		logger.info("OrderService -> getOrders() method is called. Collecting all order details");
		
		List<OrderDto> returnValue = new ArrayList<>();

		Iterable<OrderEntity> iteratableObjects = orderRepository.findAll();

		for (OrderEntity orderEntity : iteratableObjects) {
			OrderDto orderDto = new OrderDto();
			BeanUtils.copyProperties(orderEntity, orderDto);
			returnValue.add(orderDto);
		}
		
		logger.info("Orders' details found. Returning orders to the OrderController via List<OrderDto>");
		
		return returnValue;
	}

}
