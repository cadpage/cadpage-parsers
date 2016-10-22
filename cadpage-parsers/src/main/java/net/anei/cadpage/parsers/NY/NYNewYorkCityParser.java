package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYNewYorkCityParser extends FieldProgramParser {
    
    public NYNewYorkCityParser() {
      super("NEW YORK CITY", "NY",
             "ADDR! Bet:X PLACE Apt:APT? PLACE Call_ID:ID!");
    }
    
    @Override
    public String getFilter() {
      return "callid@hatzalah.org,6466933772";
    }

	  @Override
	  protected boolean parseMsg(String body, Data data) {
	    int pt = body.indexOf('\n');
	    if (pt >= 0) {
	      data.strSupp = body.substring(pt+1).trim();
	      body = body.substring(0,pt).trim();
	    }
	    body = body.replace(" Bet ", " Bet: ");
	    body = body.replace(" Between ", " Bet: ");
	    return parseFields(body.split(" -+ "), 2, data);
	  }
	  
	  @Override
	  public String getProgram() {
	    return super.getProgram() + " INFO";
	  }
	}
	