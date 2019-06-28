package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Sullivan County, NY
 */
public class NYSullivanCountyAParser extends FieldProgramParser {
  
    public NYSullivanCountyAParser() {
      super("SULLIVAN COUNTY", "NY",
             "SKIP Type:CALL! Loc:ADDR! X:X V:CITY CN:PLACE");
    }
    
    @Override
    public String getFilter() {
      return "911@co.sullivan.ny.us,messaging@iamresponding.com,777";
    }

	  @Override
	  protected boolean parseMsg(String subject, String body, Data data) {
	    
	    do {
	      if (subject.equalsIgnoreCase("911 Page")) break;
	      
	      if (subject.startsWith("Station ")) {
	        data.strSource = subject.substring(8).trim();
	        break;
	      }
	      
	      if (body.startsWith("Sullivan County 911: (911 Page)")) {
	        body = body.substring(31).trim();
	        break;
	      }
	      
	      return false;
	      
	    } while (false);
	    
	    int pt = body.indexOf('\n');
	    if (pt >= 0) body = body.substring(0,pt).trim();
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
	