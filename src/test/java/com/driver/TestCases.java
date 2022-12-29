package com.driver;

import com.driver.io.entity.FoodEntity;
import com.driver.io.entity.OrderEntity;
import com.driver.io.entity.UserEntity;
import com.driver.io.repository.FoodRepository;
import com.driver.io.repository.OrderRepository;
import com.driver.io.repository.UserRepository;
import com.driver.service.impl.FoodServiceImpl;
import com.driver.service.impl.OrderServiceImpl;
import com.driver.service.impl.UserServiceImpl;
import com.driver.shared.dto.FoodDto;
import com.driver.shared.dto.OrderDto;
import com.driver.shared.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCases {

	@InjectMocks
	FoodServiceImpl foodServiceImpl;

	@InjectMocks
	OrderServiceImpl orderServiceImpl;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	FoodRepository foodRepository;

	@Mock
	OrderRepository orderRepository;

	@Mock
	UserRepository userRepository;

	@Before
	public void setUp(){
		FoodEntity foodEntity1 = new FoodEntity();
		foodEntity1.setId(987654);
		foodEntity1.setFoodId("987654");
		foodEntity1.setFoodName("Pizza");
		foodEntity1.setFoodCategory("Fast food");
		foodEntity1.setFoodPrice(399);
		Mockito.when(foodRepository.save(any())).thenReturn(foodEntity1);
		Mockito.when(foodRepository.findByFoodId("987654")).thenReturn(foodEntity1);
		Mockito.when(foodRepository.findByFoodId("98765")).thenReturn(null);
		List<FoodEntity> foodEntityList = new ArrayList<>();
		foodEntityList.add(foodEntity1);
		Mockito.when(foodRepository.findAll()).thenReturn(foodEntityList);

		OrderEntity orderEntity1 = new OrderEntity();
		orderEntity1.setId(87654);
		orderEntity1.setOrderId("87654");
		orderEntity1.setUserId("45678");
		orderEntity1.setCost(1001);
		String [] items1 = {"Pizza", "Burger"};
		orderEntity1.setItems(items1);
		orderEntity1.setStatus(true);
		Mockito.when(orderRepository.save(any())).thenReturn(orderEntity1);
		Mockito.when(orderRepository.findByOrderId("87654")).thenReturn(orderEntity1);
		Mockito.when(orderRepository.findByOrderId("-123")).thenReturn(null);
		List<OrderEntity> orderEntityList = new ArrayList<>();
		orderEntityList.add(orderEntity1);
		Mockito.when(orderRepository.findAll()).thenReturn(orderEntityList);

		UserEntity userEntity = new UserEntity();
		userEntity.setId(1245);
		userEntity.setUserId("1245");
		userEntity.setFirstName("cio");
		userEntity.setLastName("b");
		userEntity.setEmail("cio@123.com");

		UserEntity userEntity1 = new UserEntity();
		userEntity1.setId(12345);
		userEntity1.setUserId("12345");
		userEntity1.setFirstName("Accio");
		userEntity1.setLastName("Job");
		userEntity1.setEmail("accio@123.com");
		Mockito.when(userRepository.save(any())).thenReturn(userEntity1);
		Mockito.when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
		Mockito.when(userRepository.findByEmail("error@123.com")).thenReturn(null);
		Mockito.when(userRepository.findByUserId(userEntity.getUserId())).thenReturn(userEntity);
		Mockito.when(userRepository.findByUserId(userEntity1.getUserId())).thenReturn(userEntity1);
		Mockito.when(userRepository.findByUserId("-123")).thenReturn(null);

		List<UserEntity> userEntityList = new ArrayList<>();
		userEntityList.add(userEntity1);
		Mockito.when(userRepository.findAll()).thenReturn(userEntityList);
	}

	@Test
	public void testOrderCreatedSuccess() {
		OrderDto orderDto1 = new OrderDto();
		orderDto1.setId(87654);
		orderDto1.setOrderId("87654");
		orderDto1.setUserId("45678");
		orderDto1.setCost(1001);
		String [] items1 = {"Pizza", "Burger"};
		orderDto1.setItems(items1);
		orderDto1.setStatus(true);
		OrderDto rOrderDto1 = orderServiceImpl.createOrder(orderDto1);
		verify(orderRepository, times(1)).save(any());
		assert(rOrderDto1.getId() == 87654);
		assert(rOrderDto1.getOrderId().equals("87654"));
		assert(rOrderDto1.getUserId().equals("45678"));
		assert(rOrderDto1.getCost() == 1001);
		assert(rOrderDto1.getItems().length == 2);
	}

	@Test
	public void testGetOrderByIdSuccess() {
		OrderDto rOrderDto1 = new OrderDto();
		try {
			rOrderDto1 = orderServiceImpl.getOrderById("87654");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String [] items1 = {"Pizza", "Burger"};
		verify(orderRepository, times(1)).findByOrderId(any());
		assert(rOrderDto1.getId() == 87654);
		assert(rOrderDto1.getOrderId().equals("87654"));
		assert(rOrderDto1.getUserId().equals("45678"));
		assert(rOrderDto1.getCost() == 1001);
		assert(rOrderDto1.getItems().length == 2);
		try{
			OrderDto rOrderDto2 = orderServiceImpl.getOrderById("-123");
		}
		catch (Exception e){
			assert(e.getMessage().equals("-123"));
		}
	}

	@Test
	public void testUpdateOrderDetailsSuccess() {
		OrderDto orderDto1 = new OrderDto();
		orderDto1.setId(87654);
		orderDto1.setOrderId("87654");
		orderDto1.setUserId("45678");
		orderDto1.setCost(1001);
		String [] items1 = {"Pizza", "Burger"};
		orderDto1.setItems(items1);
		orderDto1.setStatus(true);
		String [] items2 = {"Pizza"};
		orderDto1.setCost(1);
		orderDto1.setItems(items2);
		OrderDto rOrderDto1 = new OrderDto();
		try {
			rOrderDto1 = orderServiceImpl.updateOrderDetails(orderDto1.getOrderId(), orderDto1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(orderRepository, times(1)).save(any());
		verify(orderRepository, times(1)).findByOrderId(any());
		assert(rOrderDto1.getId() == 87654);
		assert(rOrderDto1.getOrderId().equals("87654"));
		assert(rOrderDto1.getUserId().equals("45678"));
		assert(rOrderDto1.getCost() == 1);
		assert(rOrderDto1.getItems().length == 1);
		try{
			OrderDto rOrderDto2 = orderServiceImpl.updateOrderDetails("-123", orderDto1);
		}
		catch (Exception e){
			assert(e.getMessage().equals("-123"));
		}
	}

	@Test
	public void testGetOrdersSuccess() {
		List<OrderDto> rOrderDtoList = orderServiceImpl.getOrders();
		verify(orderRepository, times(1)).findAll();
		assert(rOrderDtoList.size() == 1);
		OrderDto rOrderDto1 = rOrderDtoList.get(0);
		assert(rOrderDto1.getId() == 87654);
		assert(rOrderDto1.getOrderId().equals("87654"));
		assert(rOrderDto1.getUserId().equals("45678"));
		assert(rOrderDto1.getCost() == 1001);
		assert(rOrderDto1.getItems().length == 2);
	}

	@Test
	public void testFoodCreatedSuccess() {
		FoodDto foodDto1 = new FoodDto();
		foodDto1.setId(987654);
		foodDto1.setFoodId("987654");
		foodDto1.setFoodName("Pizza");
		foodDto1.setFoodCategory("Fast food");
		foodDto1.setFoodPrice(399);
		FoodDto rFoodDto1 = foodServiceImpl.createFood(foodDto1);
		verify(foodRepository, times(1)).save(any());
		assert(rFoodDto1.getId() == 987654);
		assert(rFoodDto1.getFoodId().equals("987654"));
		assert(rFoodDto1.getFoodName().equals("Pizza"));
		assert(rFoodDto1.getFoodCategory().equals("Fast food"));
		assert(rFoodDto1.getFoodPrice() == 399);
	}

	@Test
	public void testGetFoodByIdSuccess() {
		FoodDto rFoodDto1 = new FoodDto();
		try {
			rFoodDto1 = foodServiceImpl.getFoodById("987654");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(foodRepository, times(1)).findByFoodId(any());
		assert(rFoodDto1.getId() == 987654);
		assert(rFoodDto1.getFoodId().equals("987654"));
		assert(rFoodDto1.getFoodName().equals("Pizza"));
		assert(rFoodDto1.getFoodCategory().equals("Fast food"));
		assert(rFoodDto1.getFoodPrice() == 399);
		try{
			FoodDto rFoodDto2 = foodServiceImpl.getFoodById("98765");
		}
		catch (Exception e){
			assert(e.getMessage().equals("98765"));
		}
	}

	@Test
	public void testUpdateFoodDetailsSuccess() {
		FoodDto foodDto1 = new FoodDto();
		foodDto1.setId(987654);
		foodDto1.setFoodId("987654");
		foodDto1.setFoodName("Pizza");
		foodDto1.setFoodCategory("Fast food");
		foodDto1.setFoodPrice(399);
		foodDto1.setFoodName("Burger");
		foodDto1.setFoodCategory("Quick food");
		foodDto1.setFoodPrice(99);
		FoodDto rFoodDto1 = new FoodDto();
		try {
			rFoodDto1 = foodServiceImpl.updateFoodDetails(foodDto1.getFoodId(), foodDto1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(foodRepository, times(1)).save(any());
		verify(foodRepository, times(1)).findByFoodId(any());
		assert(rFoodDto1.getId() == 987654);
		assert(rFoodDto1.getFoodId().equals("987654"));
		assert(rFoodDto1.getFoodName().equals("Burger"));
		assert(rFoodDto1.getFoodCategory().equals("Quick food"));
		assert(rFoodDto1.getFoodPrice() == 99);
		try{
			FoodDto rFoodDto2 = foodServiceImpl.updateFoodDetails("98765", foodDto1);
		}
		catch (Exception e){
			assert(e.getMessage().equals("98765"));
		}
	}

	@Test
	public void testGetFoodsSuccess() {
		List<FoodDto> rFoodDtoList = foodServiceImpl.getFoods();
		verify(foodRepository, times(1)).findAll();
		assert(rFoodDtoList.size() == 1);
		FoodDto rFoodDto1 = rFoodDtoList.get(0);
		assert(rFoodDto1.getId() == 987654);
		assert(rFoodDto1.getFoodId().equals("987654"));
		assert(rFoodDto1.getFoodName().equals("Pizza"));
		assert(rFoodDto1.getFoodCategory().equals("Fast food"));
		assert(rFoodDto1.getFoodPrice() == 399);
	}

	@Test
	public void testUserCreatedSuccess() {
		UserDto userDto = new UserDto();
		userDto.setId(1245);
		userDto.setUserId("1245");
		userDto.setFirstName("cio");
		userDto.setLastName("b");
		userDto.setEmail("cio@123.com");
		UserDto userDto1 = new UserDto();
		userDto1.setId(12345);
		userDto1.setUserId("12345");
		userDto1.setFirstName("Accio");
		userDto1.setLastName("Job");
		userDto1.setEmail("accio@123.com");
		UserDto rUserDto1 = new UserDto();
		try {
			rUserDto1 = userServiceImpl.createUser(userDto1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(userRepository, times(1)).save(any());
		assert(rUserDto1.getId() == 12345);
		assert(rUserDto1.getUserId().equals("12345"));
		assert(rUserDto1.getFirstName().equals("Accio"));
		assert(rUserDto1.getLastName().equals("Job"));
		assert(rUserDto1.getEmail().equals("accio@123.com"));

		try{
			UserDto ruserDto = userServiceImpl.createUser(userDto);
		}
		catch (Exception e){
			assert(e.getMessage().equals("Record already exists!"));
		}
	}

	@Test
	public void testDeleteFoodSuccess() {
		FoodDto foodDto1 = new FoodDto();
		foodDto1.setId(987654);
		foodDto1.setFoodId("987654");
		foodDto1.setFoodName("Pizza");
		foodDto1.setFoodCategory("Fast food");
		foodDto1.setFoodPrice(399);
		FoodDto rFoodDto = foodServiceImpl.createFood(foodDto1);
		try {
			foodServiceImpl.deleteFoodItem("987654");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(foodRepository, times(1)).delete(any());
		try{
			foodServiceImpl.deleteFoodItem("98765");
		}
		catch (Exception e){
			assert(e.getMessage().equals("98765"));
		}
	}
	@Test
	public void testGetUserSuccess() {
		UserDto userDto = new UserDto();
		userDto.setId(1245);
		userDto.setUserId("1245");
		userDto.setFirstName("cio");
		userDto.setLastName("b");
		userDto.setEmail("cio@123.com");
		UserDto rUserDto1 = new UserDto();
		try {
			rUserDto1 = userServiceImpl.getUser("cio@123.com");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(userRepository, times(1)).findByEmail(any());
		assert(rUserDto1.getId() == 1245);
		assert(rUserDto1.getUserId().equals("1245"));
		assert(rUserDto1.getFirstName().equals("cio"));
		assert(rUserDto1.getLastName().equals("b"));
		assert(rUserDto1.getEmail().equals("cio@123.com"));
		try{
			UserDto rUserDto2 = userServiceImpl.getUser("error@123.com");
		}
		catch (Exception e){
			assert(e.getMessage().equals("error@123.com"));
		}
	}
	@Test
	public void testGetUserByIdSuccess() {
		UserDto rUserDto1 = new UserDto();
		try {
			rUserDto1 = userServiceImpl.getUserByUserId("1245");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(userRepository, times(1)).findByUserId(any());
		assert(rUserDto1.getId() == 1245);
		assert(rUserDto1.getUserId().equals("1245"));
		assert(rUserDto1.getFirstName().equals("cio"));
		assert(rUserDto1.getLastName().equals("b"));
		assert(rUserDto1.getEmail().equals("cio@123.com"));
		try{
			UserDto rUserDto2 = userServiceImpl.getUserByUserId("-123");
		}
		catch (Exception e){
			assert(e.getMessage().equals("-123"));
		}
	}

	@Test
	public void testUpdateUserDetailsSuccess() {
		UserDto userDto = new UserDto();
		userDto.setId(12345);
		userDto.setUserId("12345");
		userDto.setFirstName("Accio");
		userDto.setLastName("Job");
		userDto.setEmail("accio@123.com");
		userDto.setFirstName("Job");
		userDto.setLastName("Accio");
		UserDto rUserDto1 = new UserDto();
		try {
			rUserDto1 = userServiceImpl.updateUser(userDto.getUserId(), userDto);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(userRepository, times(1)).save(any());
		verify(userRepository, times(1)).findByUserId(any());
		assert(rUserDto1.getId() == 12345);
		assert(rUserDto1.getUserId().equals("12345"));
		assert(rUserDto1.getFirstName().equals("Job"));
		assert(rUserDto1.getLastName().equals("Accio"));
		assert(rUserDto1.getEmail().equals("accio@123.com"));
		try{
			UserDto rUserDto2 = userServiceImpl.updateUser("-123", userDto);
		}
		catch (Exception e){
			assert(e.getMessage().equals("-123"));
		}
	}

	@Test
	public void testDeleteUserSuccess() {
		try {
			userServiceImpl.deleteUser("1245");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		verify(userRepository, times(1)).delete(any());
		try{
			userServiceImpl.deleteUser("-123");
		}
		catch (Exception e){
			assert(e.getMessage().equals("-123"));
		}
	}

	@Test
	public void testGetUsersSuccess() {
		List<UserDto> rUserDtoList = userServiceImpl.getUsers();
		verify(userRepository, times(1)).findAll();
		assert(rUserDtoList.size() == 1);
		UserDto rUserDto1 = rUserDtoList.get(0);
		assert(rUserDto1.getId() == 12345);
		assert(rUserDto1.getUserId().equals("12345"));
		assert(rUserDto1.getFirstName().equals("Accio"));
		assert(rUserDto1.getLastName().equals("Job"));
		assert(rUserDto1.getEmail().equals("accio@123.com"));
	}
}
