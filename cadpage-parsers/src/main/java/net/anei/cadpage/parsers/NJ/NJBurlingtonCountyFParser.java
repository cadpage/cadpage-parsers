package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;



public class NJBurlingtonCountyFParser extends FieldProgramParser {


  public NJBurlingtonCountyFParser() {
    super(CITY_CODES, "BURLINGTON COUNTY", "NJ",
        "CALL! Loc:ADDR LOC:ADDR Ven:CITY VEN:CITY XS:X! Nature:INFO NATURE:INFO");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  private static final Pattern INFO_PTN = 
      Pattern.compile("[ /]* (\\d\\d:\\d\\d:\\d\\d) +(\\d\\d/\\d\\d/\\d{4})$");

  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.find()) {
        data.strTime = match.group(1);
        data.strDate = match.group(2);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO TIME DATE";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "Willingbor", "Willingboro",
      "Mt Laurel", "Mount Laurel"
  });
}

	    
	
	    
	    




	 
