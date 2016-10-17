package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Ontario County, NY
 */
public class NYOntarioCountyAParser extends FieldProgramParser {
  
    public NYOntarioCountyAParser() {
      super("ONTARIO COUNTY", "NY",
             "CANCEL? CALL TIME ADDR X UNIT! INFO/N ID");
    }
    
    @Override
    public String getFilter() {
      return "E911page@co.ontario.ny.us";
    }

	  @Override
	  protected boolean parseMsg(String body, Data data) {
	    return parseFields(body.split("\n"), data);
	  }

    @Override
    protected Field getField(String name) {
      if (name.equals("CANCEL")) return new InfoField("Cancel Reason:.*", true);
      if (name.equals("CALL")) return new MyCallField();
      if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
      if (name.equals("ADDR")) return new MyAddressField();
      if (name.equals("X")) return new MyCrossField();
      return super.getField(name);
    }
    
    private class MyCallField extends CallField {
      @Override
      public void parse(String field, Data data) {
        if (data.strSupp.startsWith("Cancel Reason:")) {
          field = "CANCEL - " + field;
        }
        super.parse(field, data);
      }
    }
	  
	  private class MyAddressField extends AddressField {
	    @Override
	    public void parse(String field, Data data) {
	      int pt = field.indexOf(',');
	      if (pt >= 0) {
	        data.strPlace = field.substring(0,pt).trim();
	        field = field.substring(pt+1).trim();
	      }
	      super.parse(field.replace(" - ", " & "), data);
	    }
	    
	    @Override
	    public String getFieldNames() {
	      return "PLACE " + super.getFieldNames();
	    }
	  }
	  
	  private class MyCrossField extends CrossField {
	    @Override
	    public void parse(String field, Data data) {
	      field = field.replaceAll(" - ", " & ");
	      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
	      super.parse(field, data);
	    }
	  }
    
    @Override
    public String adjustMapAddress(String addr) {
      addr = PK_PTN.matcher(addr).replaceAll("PARK");
      addr = RT_5_20_PTN.matcher(addr).replaceAll("5");
      addr = RT_5_21_PTN.matcher(addr).replaceAll("$1");
      return super.adjustMapAddress(addr);
    }
    private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b");
	  private static final Pattern RT_5_20_PTN = Pattern.compile("\\b5 (?:AND|&) 20\\b");
	  private static final Pattern RT_5_21_PTN = Pattern.compile("(?:RT |ROUTE |\\b)(?:5|20)(?: *& *)((?:RT|ROUTE) *21)\\b");
	}
	