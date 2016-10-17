package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NYClintonCountyParser extends FieldProgramParser {
  
    public NYClintonCountyParser() {
      super("CLINTON COUNTY", "NY",
             "Time:DATETIME! AgencyName:SRC! Address:ADDR! Resp.Type:CALL! Priority:PRI");
    }
    
    @Override
    public String getFilter() {
      return "777,888";
    }

	  @Override
	  protected boolean parseMsg(String body, Data data) {
	    return parseFields(body.split("\n"), data);
	  }
	  
	  private static final DateFormat DATE_FMT = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
	  private class MyDateTimeField extends DateTimeField {
	    @Override
	    public void parse(String field, Data data) {
	      setDateTime(DATE_FMT, field, data);
	    }
	  }
	  
	  private class MyAddressField extends AddressField {
	    @Override
	    public void parse(String field, Data data) {
	      Parser p = new Parser(field);
	      parseAddress(p.get(','), data);
	      data.strCity = p.get('(');
	      data.strCross = p.get(')');
	      
	      field = p.get();
	      while (field.startsWith("(")) {
	        p = new Parser(field.substring(1));
	        data.strCross = p.get(')');
	        field = p.get();
	      }
	    }
	    
	    @Override
	    public String getFieldNames() {
	      return "ADDR APT CITY X";
	    }
	  }
	  
	  private class MyPriorityField extends PriorityField {
	    @Override
	    public void parse(String field, Data data) {
	      if (field.startsWith("Priority ")) field = field.substring(9).trim();
	      super.parse(field, data);
	    }
	  }

    @Override
    protected Field getField(String name) {
      if (name.equals("DATETIME")) return new MyDateTimeField();
      if (name.equals("ADDR")) return new MyAddressField();
      if (name.equals("PRI")) return new MyPriorityField();
      return super.getField(name);
    }
	}
	