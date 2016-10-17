package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Albany County, NY (Colonie)
 */
public class NYAlbanyCountyAParser extends FieldProgramParser {
  
    public NYAlbanyCountyAParser() {
      super("ALBANY COUNTY", "NY",
             "CALLUNIT! ADDR! PLACE? MAP!");
    }
    
    @Override
    public String getFilter() {
      return "@colonie.org";
    }

	  @Override
	  protected boolean parseMsg(String body, Data data) {
	    return parseFields(body.split("\n"), data);
	  }
	  
	  private class MyCallUnitField extends Field {
	    @Override
	    public void parse(String field, Data data) {
	      int pt = field.lastIndexOf(' ');
	      if (pt < 0) abort();
	      data.strCall = field.substring(0,pt).trim();
	      data.strUnit = field.substring(pt+1).trim();
	    }
	    
	    @Override
	    public String getFieldNames() {
	      return "CALL UNIT";
	    }
	  }
	  
	  private class MyAddressField extends AddressField {
	    @Override
	    public void parse(String field, Data data) {
	      int pt = field.indexOf('(');
	      if (pt >= 0) field = field.substring(0,pt).trim();
	      super.parse(field, data);
	    }
	  }
	  
	  private static final Pattern MAP_PTN = 
	    Pattern.compile("Map ([A-Z ]+) (\\d\\d:\\d\\d:\\d\\d)");
	  private class MyMapField extends MapField {
	    
	    @Override
	    public boolean canFail() {
	      return true;
	    }
	    
	    @Override
	    public boolean checkParse(String field, Data data) {
        Matcher match = MAP_PTN.matcher(field);
        if (!match.matches()) return false;
        data.strMap = match.group(1).trim();
        data.strTime = match.group(2);
        return true;
	    }
	    
	    @Override
	    public void parse(String field, Data data) {
	      if (!checkParse(field, data)) abort();
	    }
	    
	    @Override
	    public String getFieldNames() {
	      return "MAP TIME";
	    }
	  }

    @Override
    protected Field getField(String name) {
      if (name.equals("CALLUNIT")) return new MyCallUnitField();
      if (name.equals("ADDR")) return new MyAddressField();
      if (name.equals("MAP")) return new MyMapField();
      return super.getField(name);
    }
	  
	}
	