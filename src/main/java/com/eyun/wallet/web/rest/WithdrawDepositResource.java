package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.WithdrawDepositService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.WithdrawDepositDTO;
import com.eyun.wallet.service.dto.PutForwardDTO;
import com.eyun.wallet.service.dto.RefuseDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.WithdrawDepositCriteria;
import com.eyun.wallet.domain.WithdrawDeposit;
import com.eyun.wallet.security.SecurityUtils;
import com.eyun.wallet.service.PushService;
import com.eyun.wallet.service.UaaService;
import com.eyun.wallet.service.WithdrawDepositQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing WithdrawDeposit.
 */
@RestController
@RequestMapping("/api")
public class WithdrawDepositResource {

    private final Logger log = LoggerFactory.getLogger(WithdrawDepositResource.class);

    private static final String ENTITY_NAME = "withdrawDeposit";

    private final WithdrawDepositService withdrawDepositService;

    private final WithdrawDepositQueryService withdrawDepositQueryService;
    
    @Autowired
    private UaaService uaaService;
    
    @Autowired
    private PushService pushService;

    public WithdrawDepositResource(WithdrawDepositService withdrawDepositService, WithdrawDepositQueryService withdrawDepositQueryService) {
        this.withdrawDepositService = withdrawDepositService;
        this.withdrawDepositQueryService = withdrawDepositQueryService;
    }

    /**
     * POST  /withdraw-deposits : Create a new withdrawDeposit.
     *
     * @param withdrawDepositDTO the withdrawDepositDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new withdrawDepositDTO, or with status 400 (Bad Request) if the withdrawDeposit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/withdraw-deposits")
    @Timed
    public ResponseEntity<WithdrawDepositDTO> createWithdrawDeposit(@RequestBody WithdrawDepositDTO withdrawDepositDTO) throws URISyntaxException {
        log.debug("REST request to save WithdrawDeposit : {}", withdrawDepositDTO);
        if (withdrawDepositDTO.getId() != null) {
            throw new BadRequestAlertException("A new withdrawDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WithdrawDepositDTO result = withdrawDepositService.save(withdrawDepositDTO);
        return ResponseEntity.created(new URI("/api/withdraw-deposits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /withdraw-deposits : Updates an existing withdrawDeposit.
     *
     * @param withdrawDepositDTO the withdrawDepositDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated withdrawDepositDTO,
     * or with status 400 (Bad Request) if the withdrawDepositDTO is not valid,
     * or with status 500 (Internal Server Error) if the withdrawDepositDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/withdraw-deposits")
    @Timed
    public ResponseEntity<WithdrawDepositDTO> updateWithdrawDeposit(@RequestBody WithdrawDepositDTO withdrawDepositDTO) throws URISyntaxException {
        log.debug("REST request to update WithdrawDeposit : {}", withdrawDepositDTO);
        if (withdrawDepositDTO.getId() == null) {
            return createWithdrawDeposit(withdrawDepositDTO);
        }
        WithdrawDepositDTO result = withdrawDepositService.save(withdrawDepositDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, withdrawDepositDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /withdraw-deposits : get all the withdrawDeposits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of withdrawDeposits in body
     */
    @GetMapping("/withdraw-deposits")
    @Timed
    public ResponseEntity<List<WithdrawDepositDTO>> getAllWithdrawDeposits(WithdrawDepositCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WithdrawDeposits by criteria: {}", criteria);
        Page<WithdrawDepositDTO> page = withdrawDepositQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/withdraw-deposits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /withdraw-deposits/:id : get the "id" withdrawDeposit.
     *
     * @param id the id of the withdrawDepositDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the withdrawDepositDTO, or with status 404 (Not Found)
     */
    @GetMapping("/withdraw-deposits/{id}")
    @Timed
    public ResponseEntity<WithdrawDepositDTO> getWithdrawDeposit(@PathVariable Long id) {
        log.debug("REST request to get WithdrawDeposit : {}", id);
        WithdrawDepositDTO withdrawDepositDTO = withdrawDepositService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(withdrawDepositDTO));
    }

    /**
     * DELETE  /withdraw-deposits/:id : delete the "id" withdrawDeposit.
     *
     * @param id the id of the withdrawDepositDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/withdraw-deposits/{id}")
    @Timed
    public ResponseEntity<Void> deleteWithdrawDeposit(@PathVariable Long id) {
        log.debug("REST request to delete WithdrawDeposit : {}", id);
        withdrawDepositService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    /**
     * 申请提现
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年5月17日
     * @version 1.0
     * @param putForwardDTO
     */
    @ApiOperation("申请提现")
    @PostMapping("/put-forward")
    public void putForward(@RequestBody PutForwardDTO putForwardDTO) {
    	UserDTO user = uaaService.getAccount();
    	withdrawDepositService.putForward(putForwardDTO,user);
    }
    
    /**
     * 提现通过
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年5月17日
     * @version 1.0
     * @param id
     */
    @ApiOperation("提现通过")
    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/put-forward/adopt/{withdrawDepositID}")
    public void putForwardAdopt(@PathVariable("withdrawDepositID") Long withdrawDepositID) {
    	WithdrawDepositDTO withdrawDepositDTO = withdrawDepositService.findOne(withdrawDepositID);
    	Instant now = Instant.now();
    	withdrawDepositDTO.setUpdatedTime(now);
    	withdrawDepositDTO.setStatus(3);
    	withdrawDepositDTO.setStatusString("提现成功");
    	withdrawDepositService.save(withdrawDepositDTO);
    	pushService.sendPushByUserid(withdrawDepositDTO.getUserid().toString(), "提现成功");
    	Optional<String> optional = SecurityUtils.getCurrentUserLogin();
    	log.info("提现操作财务员账号："+optional.get()+",提现记录id:"+withdrawDepositDTO.getId());
    }
    
    /**
     * 提现拒绝
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年5月17日
     * @version 1.0
     * @param id
     */
    @ApiOperation("提现拒绝")
    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/put-forward/refuse")
    public void putForwardRefuse(@RequestBody RefuseDTO refuseDTO) {
    	WithdrawDepositDTO withdrawDepositDTO = withdrawDepositService.findOne(refuseDTO.getId());
    	Instant now = Instant.now();
    	withdrawDepositDTO.setUpdatedTime(now);
    	withdrawDepositDTO.setStatus(2);
    	withdrawDepositDTO.setStatusString("提现失败");
    	withdrawDepositDTO.setDescribe(refuseDTO.getContent());
    	withdrawDepositService.save(withdrawDepositDTO);
    	//pushService.sendPushByUserid(withdrawDepositDTO.getUserid().toString(), "提现成功");
    	Optional<String> optional = SecurityUtils.getCurrentUserLogin();
    	log.info("提现操作财务员账号："+optional.get()+",提现记录id:"+withdrawDepositDTO.getId());
    }
    
	@ApiOperation("提现明细")
	@GetMapping("/wallet/withdawDetil")
	public ResponseEntity<Object> getAllWithDetil(HttpServletResponse resp){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("提现明细");
		//sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
		HSSFRow row2 = sheet.createRow(0);
		row2.createCell(0).setCellValue("姓名");  
	    row2.createCell(1).setCellValue("状态");      
	    row2.createCell(2).setCellValue("银行卡号");  
	    row2.createCell(3).setCellValue("开户银行");
	    row2.createCell(4).setCellValue("金额");
	    
	    List<WithdrawDeposit> wd = withdrawDepositService.findAll();
	    int size = wd.size();
	    for(int i = 0; i<size ; i++){
	    	HSSFRow row3 = sheet.createRow(i+1);
	    	WithdrawDeposit withdrawDeposit = wd.get(i);
	    	if(withdrawDeposit.getCardholder()==null){
		    	row3.createCell(0).setCellValue("");
	    	}else{
		    	row3.createCell(0).setCellValue(withdrawDeposit.getCardholder());
	    	}
	    	if(withdrawDeposit.getStatus()==null){
		    	row3.createCell(1).setCellValue("");
	    	}else{
		    	row3.createCell(1).setCellValue(withdrawDeposit.getStatusString());
	    	}
	    	if(withdrawDeposit.getBankcardNumber()==null){
		    	row3.createCell(2).setCellValue("");
	    	}else{
		    	row3.createCell(2).setCellValue(withdrawDeposit.getBankcardNumber());
	    	}
	    	if(withdrawDeposit.getOpeningBank()==null){
		    	row3.createCell(3).setCellValue("");
	    	}else{
		    	row3.createCell(3).setCellValue(withdrawDeposit.getOpeningBank());
	    	}
	    	if(withdrawDeposit.getMoney()==null){
		    	row3.createCell(4).setCellValue("");
	    	}else{
		    	row3.createCell(4).setCellValue(withdrawDeposit.getMoney().toString());
	    	}	           
	    }
	   
	    try {
			OutputStream output = resp.getOutputStream();
			resp.reset();
			String filename = "提现明细";
			resp.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			resp.setCharacterEncoding("utf-8");  
			resp.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
			
			wb.write(output);  
		    output.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return ResponseEntity.ok().body(null);	    	
	}
	
	
	@ApiOperation("分段提现明细")
	@GetMapping("/wallet/subwithdawDetil/{first}/{last}")
	public ResponseEntity<Object> getSubAllWithDetil(@PathVariable("first") String first,@PathVariable("last") String last,HttpServletResponse resp){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("提现明细");
		//sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
		HSSFRow row2 = sheet.createRow(0);
		row2.createCell(0).setCellValue("姓名");  
	    row2.createCell(1).setCellValue("状态");      
	    row2.createCell(2).setCellValue("银行卡号");  
	    row2.createCell(3).setCellValue("开户银行");
	    row2.createCell(4).setCellValue("金额");
	    row2.createCell(5).setCellValue("提现时间");
	    System.out.println("-------------first的值" + first + "******");
	    List<WithdrawDeposit> wd ;
	    if(last == null || last == ""){
		    wd = withdrawDepositService.findRigDetil(first);
		    System.out.println("last 为空");
	    }else if(first == null || first == ""){
		    wd = withdrawDepositService.findLefDetil(last);
		    System.out.println("first 为空");
	    }else{
		    wd = withdrawDepositService.findSubDetil(first, last);
		    System.out.println("都不为为空");
	    }
	    int size = wd.size();
	    for(int i = 0; i<size ; i++){
	    	HSSFRow row3 = sheet.createRow(i+1);
	    	WithdrawDeposit withdrawDeposit = wd.get(i);
	    	if(withdrawDeposit.getCardholder()==null){
		    	row3.createCell(0).setCellValue("");
	    	}else{
		    	row3.createCell(0).setCellValue(withdrawDeposit.getCardholder());
	    	}
	    	if(withdrawDeposit.getStatus()==null){
		    	row3.createCell(1).setCellValue("");
	    	}else{
		    	row3.createCell(1).setCellValue(withdrawDeposit.getStatusString());
	    	}
	    	if(withdrawDeposit.getBankcardNumber()==null){
		    	row3.createCell(2).setCellValue("");
	    	}else{
		    	row3.createCell(2).setCellValue(withdrawDeposit.getBankcardNumber());
	    	}
	    	if(withdrawDeposit.getOpeningBank()==null){
		    	row3.createCell(3).setCellValue("");
	    	}else{
		    	row3.createCell(3).setCellValue(withdrawDeposit.getOpeningBank());
	    	}
	    	if(withdrawDeposit.getMoney()==null){
		    	row3.createCell(4).setCellValue("");
	    	}else{
		    	row3.createCell(4).setCellValue(withdrawDeposit.getMoney().toString());
	    	}
	    	if(withdrawDeposit.getCreatedTime()==null){
		    	row3.createCell(5).setCellValue("");
	    	}else{
			    row3.createCell(5).setCellValue(withdrawDeposit.getCreatedTime().toString());		
	    	}	
	    }
	   
	    try {
			OutputStream output = resp.getOutputStream();
			resp.reset();
			String filename = "提现明细";
			resp.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			resp.setCharacterEncoding("utf-8");  
			resp.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
			
			wb.write(output);  
		    output.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return ResponseEntity.ok().body(null);	    	
	}
	
	
	
	
	
	
	
    
}
