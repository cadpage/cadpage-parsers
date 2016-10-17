package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Sullivan County, NY
 */
public class NYSullivanCountyParser extends FieldProgramParser {
  
    public NYSullivanCountyParser() {
      super("SULLIVAN COUNTY", "NY",
             "SKIP Type:CALL! Loc:ADDR! X:X V:CITY CN:NAME");
    }
    
    @Override
    public String getFilter() {
      return "911@co.sullivan.ny.us,messaging@iamresponding.com";
    }

	  @Override
	  protected boolean parseMsg(String subject, String body, Data data) {
	    do {
	      if (subject.equalsIgnoreCase("911 Page")) break;
	      
	      if (subject.startsWith("Station ")) {
	        data.strSource = subject.substring(8).trim();
	        break;
	      }
	      
	      return false;
	      
	    } while (false);
	    return super.parseMsg(body, data);
	  }
	  
	  @Override
	  public String getProgram() {
	    return "SRC " + super.getProgram();
	  }
	  
	  class MyCityField extends CityField {
	    
	    @Override
	    public void parse(String fld, Data data) {
	      if (fld.startsWith("V/")) fld = fld.substring(2).trim();
	      super.parse(fld, data);
	    }
	  }

    @Override
    protected Field getField(String name) {
      if (name.equals("CITY")) return new MyCityField();
      return super.getField(name);
    }
	  
	}
	