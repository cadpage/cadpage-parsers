package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYWayneCountyAParser extends FieldProgramParser {
    
    public NYWayneCountyAParser() {
      super("WAYNE COUNTY", "NY",
             "( DISPATCH | ALARM_CLOSE/R ) TIME CALL ADDR! ( PLACE_APT NAME PHONE | PLACENAME? X PLACE_APT? PHONE ) INFO+");
    }
    
    @Override
    public String getFilter() {
      return "newarkamb@fdcms.info,williamsonfireco@fdcms.info,contari1@rochester.rr.com,Dispatch@marionfiredept.com";
    }

	  @Override
	  protected boolean parseMsg(String subject, String body, Data data) {
	    body = body + " ";
	    return parseFields(body.split(" \\*\\* "), data);
	  }

    @Override
    protected Field getField(String name) {
      if (name.equals("DISPATCH")) return new SkipField("Dispatch", true);
      if (name.equals("ALARM_CLOSE")) return new SkipField("Alarm Close", true);
      if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d|", true);
      if (name.equals("PLACE_APT")) return new MyPlaceAptField();
      if (name.equals("NAME")) return new MyNameField();
      if (name.equals("X")) return new MyCrossField();
      if (name.equals("INFO")) return new MyInfoField();
      return super.getField(name);
    }
    
    private static final Pattern PHONE_PTN = Pattern.compile("[0-9]+-[-0-9]*");
    
    private class MyPlaceAptField extends Field {
      
      @Override
      public boolean canFail() {
        return true;
      }
      
      @Override
      public boolean checkParse(String field, Data data) {
        
        // Expect a dash separating the place name and apt
        // But do not accept anything that looks like a phone number
        int pt = field.indexOf('-');
        if (pt < 0) return false;
        if (PHONE_PTN.matcher(field).matches()) return false;
        data.strPlace = append(data.strPlace, " - ", field.substring(0,pt).trim());
        String apt = field.substring(pt+1).trim();
        apt = stripFieldStart(apt, "Apt:");
        data.strApt = append(data.strApt, "-", apt);
        return true;
      }

      @Override
      public void parse(String field, Data data) {
        if (!checkParse(field, data)) abort();
      }

      @Override
      public String getFieldNames() {
        return "PLACE APT";
      }
    }
    
    private class MyNameField extends NameField {
      @Override 
      public void parse(String field, Data data) {
        if (field.toUpperCase().endsWith(" COUNTY")) {
          data.strCity = field;
        } else {
          super.parse(field, data);
        }
      }
    }
	  
	  // Cross street field might have neighboring county
	  private class MyCrossField extends CrossField {
	    @Override
	    public boolean canFail() {
	      return true;
	    }
	    
	    @Override
	    public boolean checkParse(String field, Data data) {
	      if (field.contains(" * ")) {
          field  = field.replace('*', '&');
          super.parse(field, data);
	      } else return false;
	      return true;
	    }
	    
	    @Override
	    public void parse(String field, Data data) {
	      if (field.toUpperCase().endsWith(" COUNTY")) {
	        data.strCity = field;
	      }  else {
	        field  = field.replace('*', '&');
	        super.parse(field, data);
	      }
	    }
	    
	    @Override
	    public String getFieldNames() {
	      return "X CITY";
	    }
	  }
	  
	  private class MyInfoField extends InfoField {
	    @Override
	    public void parse(String field, Data data) {
	      for (String part : field.split("\n")) {
	        part = part.trim();
	        
	        if (part.startsWith("incident=")) {
	          data.strCallId = part.substring(9).trim();
	          continue;
	        }
	        
	        if (part.startsWith("operator=")) continue;
	        
	        if (part.startsWith("location=")) {
	          if (data.strAddress.length() == 0) {
	            parseAddress(part.substring(9).trim(), data);
	          }
	          continue;
	        }
          
          if (part.startsWith("callback=")) {
            if (data.strPhone.length() == 0) {
              data.strPhone = part.substring(9).trim();
            }
            continue;
          }
          
          if (part.startsWith("problem=")) part = part.substring(8).trim();
          
          data.strSupp = append(data.strSupp, "\n", part);
	      }
	    }
	    
	    @Override
	    public String getFieldNames() {
	      return "ID ADDR APT PHONE INFO";
	    }
	  }
	}
	