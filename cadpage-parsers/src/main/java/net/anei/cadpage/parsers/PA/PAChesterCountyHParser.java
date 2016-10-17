package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyHParser extends PAChesterCountyBaseParser {

  public PAChesterCountyHParser() {
    super("TIME CALL ADDR! PLACE? NAME/Z PHONE/Z BOX INFO+? CITY INFO+");
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    // remove Dispatch\n. If message doesn't start with it, fail.
    if (!body.startsWith("Dispatch\n")) return false;
    body = body.substring(9).trim();
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("(.*?)(?: \\*)?");
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("APT")) return new AptField("(?:APT|RM|ROOM) *(.*)");
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }
  
  private static final Pattern NAME_UNIT_PTN = Pattern.compile("[A-Z]+\\d+");
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (NAME_UNIT_PTN.matcher(field).matches()) {
        data.strUnit = field;
      } else {
        data.strName = append(data.strName, " / ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME UNIT";
    }
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      data.strPhone = append(data.strPhone, " / ", field);
    }
  }
  
  //place field should detect and relay information better suited to other fields (name, phone, apt)
  private static Pattern PLACE_PARSER = Pattern.compile("(.*?)(?:([A-Z]+,? [A-Z]+|[A-Z]+/[A-Z]+)/)?(\\d{3}-\\d{3}-\\d{3,4})?(?:(?:APT|RM|ROOM) ([^ ]+))?");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = PLACE_PARSER.matcher(field);
      if (mat.matches()) {
        field = getOptGroup(mat.group(1)).trim();
        data.strName = getOptGroup(mat.group(2)).replace("/", " ");
        data.strPhone = getOptGroup(mat.group(3));
        data.strApt = append(data.strApt, " / ", getOptGroup(mat.group(4)));
      }
      data.strPlace = append(data.strPlace, " / ", field);
    }
  }

  //ignore DETAILS TO FOLLOW
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("DETAILS TO FOLLOW")) return;
      super.parse(field, data);
    }
  }

}
