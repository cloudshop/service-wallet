package com.eyun.wallet.web.rest;

import com.eyun.wallet.WalletApp;

import com.eyun.wallet.config.SecurityBeanOverrideConfiguration;

import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.PayOrderRepository;
import com.eyun.wallet.service.PayOrderService;
import com.eyun.wallet.service.dto.PayOrderDTO;
import com.eyun.wallet.service.mapper.PayOrderMapper;
import com.eyun.wallet.web.rest.errors.ExceptionTranslator;
import com.eyun.wallet.service.dto.PayOrderCriteria;
import com.eyun.wallet.service.PayOrderQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PayOrderResource REST controller.
 *
 * @see PayOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class PayOrderResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final String DEFAULT_PAY_NO = "AAAAAAAAAA";
    private static final String UPDATED_PAY_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_NO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INTEGRAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEGRAL = new BigDecimal(2);

    private static final Instant DEFAULT_PAY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PayOrderRepository payOrderRepository;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayOrderQueryService payOrderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPayOrderMockMvc;

    private PayOrder payOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayOrderResource payOrderResource = new PayOrderResource(payOrderService, payOrderQueryService);
        this.restPayOrderMockMvc = MockMvcBuilders.standaloneSetup(payOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayOrder createEntity(EntityManager em) {
        PayOrder payOrder = new PayOrder()
            .userid(DEFAULT_USERID)
            .payNo(DEFAULT_PAY_NO)
            .orderNo(DEFAULT_ORDER_NO)
            .balance(DEFAULT_BALANCE)
            .ticket(DEFAULT_TICKET)
            .integral(DEFAULT_INTEGRAL)
            .payTime(DEFAULT_PAY_TIME);
        return payOrder;
    }

    @Before
    public void initTest() {
        payOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayOrder() throws Exception {
        int databaseSizeBeforeCreate = payOrderRepository.findAll().size();

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);
        restPayOrderMockMvc.perform(post("/api/pay-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeCreate + 1);
        PayOrder testPayOrder = payOrderList.get(payOrderList.size() - 1);
        assertThat(testPayOrder.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testPayOrder.getPayNo()).isEqualTo(DEFAULT_PAY_NO);
        assertThat(testPayOrder.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);
        assertThat(testPayOrder.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testPayOrder.getTicket()).isEqualTo(DEFAULT_TICKET);
        assertThat(testPayOrder.getIntegral()).isEqualTo(DEFAULT_INTEGRAL);
        assertThat(testPayOrder.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
    }

    @Test
    @Transactional
    public void createPayOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payOrderRepository.findAll().size();

        // Create the PayOrder with an existing ID
        payOrder.setId(1L);
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayOrderMockMvc.perform(post("/api/pay-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPayOrders() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList
        restPayOrderMockMvc.perform(get("/api/pay-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.toString())));
    }

    @Test
    @Transactional
    public void getPayOrder() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get the payOrder
        restPayOrderMockMvc.perform(get("/api/pay-orders/{id}", payOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payOrder.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.payNo").value(DEFAULT_PAY_NO.toString()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.ticket").value(DEFAULT_TICKET.intValue()))
            .andExpect(jsonPath("$.integral").value(DEFAULT_INTEGRAL.intValue()))
            .andExpect(jsonPath("$.payTime").value(DEFAULT_PAY_TIME.toString()));
    }

    @Test
    @Transactional
    public void getAllPayOrdersByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where userid equals to DEFAULT_USERID
        defaultPayOrderShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the payOrderList where userid equals to UPDATED_USERID
        defaultPayOrderShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultPayOrderShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the payOrderList where userid equals to UPDATED_USERID
        defaultPayOrderShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where userid is not null
        defaultPayOrderShouldBeFound("userid.specified=true");

        // Get all the payOrderList where userid is null
        defaultPayOrderShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where userid greater than or equals to DEFAULT_USERID
        defaultPayOrderShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the payOrderList where userid greater than or equals to UPDATED_USERID
        defaultPayOrderShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where userid less than or equals to DEFAULT_USERID
        defaultPayOrderShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the payOrderList where userid less than or equals to UPDATED_USERID
        defaultPayOrderShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllPayOrdersByPayNoIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payNo equals to DEFAULT_PAY_NO
        defaultPayOrderShouldBeFound("payNo.equals=" + DEFAULT_PAY_NO);

        // Get all the payOrderList where payNo equals to UPDATED_PAY_NO
        defaultPayOrderShouldNotBeFound("payNo.equals=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByPayNoIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payNo in DEFAULT_PAY_NO or UPDATED_PAY_NO
        defaultPayOrderShouldBeFound("payNo.in=" + DEFAULT_PAY_NO + "," + UPDATED_PAY_NO);

        // Get all the payOrderList where payNo equals to UPDATED_PAY_NO
        defaultPayOrderShouldNotBeFound("payNo.in=" + UPDATED_PAY_NO);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByPayNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payNo is not null
        defaultPayOrderShouldBeFound("payNo.specified=true");

        // Get all the payOrderList where payNo is null
        defaultPayOrderShouldNotBeFound("payNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByOrderNoIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where orderNo equals to DEFAULT_ORDER_NO
        defaultPayOrderShouldBeFound("orderNo.equals=" + DEFAULT_ORDER_NO);

        // Get all the payOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultPayOrderShouldNotBeFound("orderNo.equals=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByOrderNoIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where orderNo in DEFAULT_ORDER_NO or UPDATED_ORDER_NO
        defaultPayOrderShouldBeFound("orderNo.in=" + DEFAULT_ORDER_NO + "," + UPDATED_ORDER_NO);

        // Get all the payOrderList where orderNo equals to UPDATED_ORDER_NO
        defaultPayOrderShouldNotBeFound("orderNo.in=" + UPDATED_ORDER_NO);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByOrderNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where orderNo is not null
        defaultPayOrderShouldBeFound("orderNo.specified=true");

        // Get all the payOrderList where orderNo is null
        defaultPayOrderShouldNotBeFound("orderNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where balance equals to DEFAULT_BALANCE
        defaultPayOrderShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the payOrderList where balance equals to UPDATED_BALANCE
        defaultPayOrderShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultPayOrderShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the payOrderList where balance equals to UPDATED_BALANCE
        defaultPayOrderShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where balance is not null
        defaultPayOrderShouldBeFound("balance.specified=true");

        // Get all the payOrderList where balance is null
        defaultPayOrderShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where ticket equals to DEFAULT_TICKET
        defaultPayOrderShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the payOrderList where ticket equals to UPDATED_TICKET
        defaultPayOrderShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultPayOrderShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the payOrderList where ticket equals to UPDATED_TICKET
        defaultPayOrderShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where ticket is not null
        defaultPayOrderShouldBeFound("ticket.specified=true");

        // Get all the payOrderList where ticket is null
        defaultPayOrderShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByIntegralIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where integral equals to DEFAULT_INTEGRAL
        defaultPayOrderShouldBeFound("integral.equals=" + DEFAULT_INTEGRAL);

        // Get all the payOrderList where integral equals to UPDATED_INTEGRAL
        defaultPayOrderShouldNotBeFound("integral.equals=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByIntegralIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where integral in DEFAULT_INTEGRAL or UPDATED_INTEGRAL
        defaultPayOrderShouldBeFound("integral.in=" + DEFAULT_INTEGRAL + "," + UPDATED_INTEGRAL);

        // Get all the payOrderList where integral equals to UPDATED_INTEGRAL
        defaultPayOrderShouldNotBeFound("integral.in=" + UPDATED_INTEGRAL);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByIntegralIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where integral is not null
        defaultPayOrderShouldBeFound("integral.specified=true");

        // Get all the payOrderList where integral is null
        defaultPayOrderShouldNotBeFound("integral.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByPayTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payTime equals to DEFAULT_PAY_TIME
        defaultPayOrderShouldBeFound("payTime.equals=" + DEFAULT_PAY_TIME);

        // Get all the payOrderList where payTime equals to UPDATED_PAY_TIME
        defaultPayOrderShouldNotBeFound("payTime.equals=" + UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByPayTimeIsInShouldWork() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payTime in DEFAULT_PAY_TIME or UPDATED_PAY_TIME
        defaultPayOrderShouldBeFound("payTime.in=" + DEFAULT_PAY_TIME + "," + UPDATED_PAY_TIME);

        // Get all the payOrderList where payTime equals to UPDATED_PAY_TIME
        defaultPayOrderShouldNotBeFound("payTime.in=" + UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllPayOrdersByPayTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);

        // Get all the payOrderList where payTime is not null
        defaultPayOrderShouldBeFound("payTime.specified=true");

        // Get all the payOrderList where payTime is null
        defaultPayOrderShouldNotBeFound("payTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPayOrdersByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        Wallet wallet = WalletResourceIntTest.createEntity(em);
        em.persist(wallet);
        em.flush();
        payOrder.setWallet(wallet);
        payOrderRepository.saveAndFlush(payOrder);
        Long walletId = wallet.getId();

        // Get all the payOrderList where wallet equals to walletId
        defaultPayOrderShouldBeFound("walletId.equals=" + walletId);

        // Get all the payOrderList where wallet equals to walletId + 1
        defaultPayOrderShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPayOrderShouldBeFound(String filter) throws Exception {
        restPayOrderMockMvc.perform(get("/api/pay-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].payNo").value(hasItem(DEFAULT_PAY_NO.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(DEFAULT_TICKET.intValue())))
            .andExpect(jsonPath("$.[*].integral").value(hasItem(DEFAULT_INTEGRAL.intValue())))
            .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPayOrderShouldNotBeFound(String filter) throws Exception {
        restPayOrderMockMvc.perform(get("/api/pay-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPayOrder() throws Exception {
        // Get the payOrder
        restPayOrderMockMvc.perform(get("/api/pay-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayOrder() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();

        // Update the payOrder
        PayOrder updatedPayOrder = payOrderRepository.findOne(payOrder.getId());
        // Disconnect from session so that the updates on updatedPayOrder are not directly saved in db
        em.detach(updatedPayOrder);
        updatedPayOrder
            .userid(UPDATED_USERID)
            .payNo(UPDATED_PAY_NO)
            .orderNo(UPDATED_ORDER_NO)
            .balance(UPDATED_BALANCE)
            .ticket(UPDATED_TICKET)
            .integral(UPDATED_INTEGRAL)
            .payTime(UPDATED_PAY_TIME);
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(updatedPayOrder);

        restPayOrderMockMvc.perform(put("/api/pay-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isOk());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate);
        PayOrder testPayOrder = payOrderList.get(payOrderList.size() - 1);
        assertThat(testPayOrder.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testPayOrder.getPayNo()).isEqualTo(UPDATED_PAY_NO);
        assertThat(testPayOrder.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);
        assertThat(testPayOrder.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testPayOrder.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testPayOrder.getIntegral()).isEqualTo(UPDATED_INTEGRAL);
        assertThat(testPayOrder.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingPayOrder() throws Exception {
        int databaseSizeBeforeUpdate = payOrderRepository.findAll().size();

        // Create the PayOrder
        PayOrderDTO payOrderDTO = payOrderMapper.toDto(payOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPayOrderMockMvc.perform(put("/api/pay-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the PayOrder in the database
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePayOrder() throws Exception {
        // Initialize the database
        payOrderRepository.saveAndFlush(payOrder);
        int databaseSizeBeforeDelete = payOrderRepository.findAll().size();

        // Get the payOrder
        restPayOrderMockMvc.perform(delete("/api/pay-orders/{id}", payOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PayOrder> payOrderList = payOrderRepository.findAll();
        assertThat(payOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayOrder.class);
        PayOrder payOrder1 = new PayOrder();
        payOrder1.setId(1L);
        PayOrder payOrder2 = new PayOrder();
        payOrder2.setId(payOrder1.getId());
        assertThat(payOrder1).isEqualTo(payOrder2);
        payOrder2.setId(2L);
        assertThat(payOrder1).isNotEqualTo(payOrder2);
        payOrder1.setId(null);
        assertThat(payOrder1).isNotEqualTo(payOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayOrderDTO.class);
        PayOrderDTO payOrderDTO1 = new PayOrderDTO();
        payOrderDTO1.setId(1L);
        PayOrderDTO payOrderDTO2 = new PayOrderDTO();
        assertThat(payOrderDTO1).isNotEqualTo(payOrderDTO2);
        payOrderDTO2.setId(payOrderDTO1.getId());
        assertThat(payOrderDTO1).isEqualTo(payOrderDTO2);
        payOrderDTO2.setId(2L);
        assertThat(payOrderDTO1).isNotEqualTo(payOrderDTO2);
        payOrderDTO1.setId(null);
        assertThat(payOrderDTO1).isNotEqualTo(payOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(payOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(payOrderMapper.fromId(null)).isNull();
    }
}
