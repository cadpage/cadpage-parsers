package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyD4Parser extends PAChesterCountyBaseParser {

  public PAChesterCountyD4Parser() {
    super("( SELECT/TEXT TIME? CALL ADDR BOX EMPTY+? DUP_ADDR! " +
          "| DISPATCH TIME CALL EMPTY? ( ADDRCITY! | ADDR! ) PLACE ( NAME PHONE! BOX? | NAME PHONE/Z BOX! | BOX | NAME ) EMPTY+? CITY? INFO/N+? ( DETAILS DATE? | DATE ) ( DUP_CALL ID? SKIP+? CITY | CITY_E ) EMPTY+? UNIT? EMPTY+? ( PLACE X | X | ) " +
          "| DATE TIME DISPATCH CALL ADDR PLACE CITY X NAME! PHONE BOX EMPTY INFO ID " +
          "| SKIP+? DISPATCH TIME CALL ADDR PLACE BOX EMPTY DATE CITY SKIP X TIME! " +
          ")");
  }

  @Override
  public String getFilter() {
    return "lcfc73@fdcms.info,adi62@ridgefirecompany.com,adi62P@ridgefirecompany.com,firepaging@comcast.net,49dispatch@gmail.com,dispatch@ebfc49.org,87emsdispatch@gmail.com,FameDispatch@fdcms2.info,kimbertonfire@gmail.com,dispatch@diverescue77.org,libertyfc@fdcmsincidents.com";
  }

  private static final Pattern TEXT_PTN = Pattern.compile("[A-Z]+ / +");
  private static final Pattern START_TIME_PTN = Pattern.compile("\\d\\d:\\d\\d \\*\\* ");
  private static final Pattern DELIM = Pattern.compile(" \\*\\*?(?= |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Eliminate PAChesterCountyO alerts.  These are dual format alerts, they start out looking like
    // PAChesterCountyD4 alerts but have PAChesterCountyO information appended
    if (body.contains("Chester County Emergency Services Dispatch Report\n\nCall Time:")) return false;

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
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d(?::\\d\\d)?", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if  (name.equals("DUP_ADDR")) return new MyDupAddressField();
    if (name.equals("X")) return new CrossField(".* AND .*");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:\\d{2}|\\d{2,3}[A-Z]|[A-Z]+\\d+)\\b,?)*", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DETAILS")) return new SkipField("DETAILS TO FOLLOW", true);
    if (name.equals("CITY_E")) return new MyCityEmptyField();
    if (name.equals("ID")) return new IdField("[EF]\\d{8}|", true);
    if (name.equals("DUP_CALL")) return new MyDuplicateCallField();
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

  private static final Pattern INFO_HEADER_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d [a-z]{2}\\d{1,3} +");
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
        for (String line : field.split("\n")) {
          line = line.trim();
          Matcher match = INFO_HEADER_PTN.matcher(line);
          if (match.lookingAt()) line = line.substring(match.end());
          data.strSupp = append(data.strSupp, "\n", line);
        }
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

  private class MyCityEmptyField extends MyCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }
  }

  private class MyDuplicateCallField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      String call = stripFieldStart(data.strCall, "F-");
      if (call.equals(field)) return true;
      if (field.length() >= 6 && call.startsWith(field)) return true;
      return false;
    }
  }
}
