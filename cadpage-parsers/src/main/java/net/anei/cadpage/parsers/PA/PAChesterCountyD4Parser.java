package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyD4Parser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyD4Parser() {
    super("( SELECT/TEXT TIME? CALL ADDR BOX EMPTY+? DUP_ADDR! " + 
          "| DISPATCH TIME CALL EMPTY? ( ADDRCITY! | ADDR! ) PLACE ( NAME PHONE! BOX? | NAME PHONE/Z BOX! | BOX | NAME ) EMPTY+? CITY? INFO/N+? ( DETAILS DATE? | DATE ) CITY UNIT? X INFO/N+ " + 
          ")");
  }
  
  @Override
  public String getFilter() {
    return "lcfc73@fdcms.info,adi62@ridgefirecompany.com,adi62P@ridgefirecompany.com,firepaging@comcast.net,49dispatch@gmail.com,dispatch@ebfc49.org";
  }

  private static final Pattern TEXT_PTN = Pattern.compile("[A-Z]+ / +");
  private static final Pattern START_TIME_PTN = Pattern.compile("\\d\\d:\\d\\d \\*\\* "); 
  private static final Pattern DETAILS_TO_FOLLOW = Pattern.compile("(?:\n| \\*\\* )(?: *DETAILS TO FOLLOW|TYP:).*? \\*\\*|\\n +\\*\\*", Pattern.DOTALL);
  private static final Pattern DELIM = Pattern.compile(" \\*\\*?(?= |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Eliminate PAChesterCountyO alerts.  These are dual format alerts, they start out looking like
    // PAChesterCountyD4 alerts but have PAChesterCountyO information appended
    if (body.contains("Chester County Emergency Services Dispatch Report")) return false;
    
    Matcher match = TEXT_PTN.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("TEXT");
      body = body.substring(match.end());
    } else {
      setSelectValue("");
      
      match = START_TIME_PTN.matcher(body);
      if (match.lookingAt()) {
        String search = " ** Dispatch ** " + match.group();
        int pt = body.indexOf(search);
        if (pt < 0) return false;
        body = "Dispatch ** " + body.substring(0,pt).trim();
      }
     
      body = stripFieldStart(body, "Dispatch / ");
      body = DETAILS_TO_FOLLOW.matcher(body).replaceFirst(" ** DETAILS TO FOLLOW **");
      body = body.replace("\n", "");
    }
    return parseFields(DELIM.split(body, -1), data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT X CITY";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DISPATCH")) return new SkipField("Dispatch", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if  (name.equals("DUP_ADDR")) return new MyDupAddressField();
    if (name.equals("X")) return new CrossField("XXX.*&.*|");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    if (name.equals("UNIT")) return new UnitField("\\d{2}|", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DETAILS")) return new SkipField("DETAILS TO FOLLOW", true);
    return super.getField(name);
  }
  
  private String saveAddress = "";
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      saveAddress = field;
      super.parse(field, data);
    }
  }
  
  private class MyDupAddressField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(saveAddress);
    }
  }
  
  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("#(\\d{3}[- ]\\d{3}[- ]\\d{4})\\b[- ]*(.*)");
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(?:APT|RM|#)[- ]*([^-, ]+(?:-\\d+)?)[-, ]*(.*)");
  private class MyPlaceField extends PlaceField {
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      field = stripFieldEnd(field, "-");
      
      Matcher match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        field = match.group(2);
      }
      
      else if ((match = PLACE_APT_PTN.matcher(field)).matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE APT PLACE";
    }
  }
  
  private class MyNameField extends NameField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyPhoneField extends PhoneField {
    public MyPhoneField() {
      super("C?\\d{3}[-.]\\d{3}[-.]\\d{3,4}|\\(\\d{3}\\) ?\\d{3}-\\d{1,4}");
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("DETAILS TO FOLLOW")) return;
      if (field.startsWith("ADR:")) return;
      if (field.startsWith("LOC:")) return;
      if (field.startsWith("NAM:")) {
        if (data.strName.length() == 0) data.strName = field.substring(4).trim();
      } else if (field.startsWith("LOCI:")) {
        if (data.strPlace.length() == 0) data.strPlace = field.substring(5).trim();
      } else if (field.startsWith("PHO:")) {
        if (data.strPhone.length() == 0) data.strPhone = field.substring(4).trim();
      } else {
        if (field.startsWith("TXT:")) field = field.substring(4).trim();
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PLACE PHONE INFO";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      data.strCity = convertCodes(field, CITY_CODES);
    }
  }
} 
