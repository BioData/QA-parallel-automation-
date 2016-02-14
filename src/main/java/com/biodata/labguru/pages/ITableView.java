package com.biodata.labguru.pages;

import java.util.List;


/**
 * Interface which represent all modules that have table presentation for their items
 * (e.g.:bio collections,boxes,stocks,cryogenic canes,equipments)
 * 
 * @author goni
 *
 */
public interface ITableView {

	public List<String> getAvailableColumnsForCustomiseTableView();
	
	public String checkCustomizeTableView() throws InterruptedException;
	
	public boolean hasList();
	
	public void deleteAllItemsFromTable() throws InterruptedException;
}
