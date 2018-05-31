package com.eyun.wallet.service;

import com.eyun.wallet.domain.WithdrawDeposit;
import com.eyun.wallet.service.dto.PutForwardDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.WithdrawDepositDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WithdrawDeposit.
 */
public interface WithdrawDepositService {

    /**
     * Save a withdrawDeposit.
     *
     * @param withdrawDepositDTO the entity to save
     * @return the persisted entity
     */
    WithdrawDepositDTO save(WithdrawDepositDTO withdrawDepositDTO);

    /**
     * Get all the withdrawDeposits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WithdrawDepositDTO> findAll(Pageable pageable);

    /**
     * Get the "id" withdrawDeposit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WithdrawDepositDTO findOne(Long id);

    /**
     * Delete the "id" withdrawDeposit.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	void putForward(PutForwardDTO putForwardDTO, UserDTO user);
	
    public List<WithdrawDeposit> findAll();

    public List<WithdrawDeposit> findSubDetil(String first,String last);
    
	public List<WithdrawDeposit> findLefDetil(String last);
	
	public List<WithdrawDeposit> findRigDetil(String first);
}
