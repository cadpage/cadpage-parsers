package net.anei.cadpage.parsers.NY;

/**
 * Suffolk County (variant F)
 */
public class NYSuffolkCountyIParser extends NYSuffolkCountyXBaseParser {
  
    public NYSuffolkCountyIParser() {
      super("SUFFOLK COUNTY", "NY",
             "CALL PLACE ADDR/S6 CITY TOA:TOA SRC ID INFO+");
    }
    
    @Override
    public String getFilter() {
      return "amityvillefdpaging@gmail.com,amityvillefdpaging1@gmail.com";
    }
	}
	