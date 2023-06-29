package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA72Parser extends FieldProgramParser {

  public DispatchA72Parser(String defCity, String defState) {
    super(defCity, defState,
        "CFS:ID! CALLTYPE:CALL! PRIORITY:PRI! PLACE:PLACE! ADDRESS:ADDR! CITY:CITY! STATE:ST! ZIP:ZIP! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! INFO/N+ ALERT:ALERT");
  }

  static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(?:(\\S*\\d\\S*) +)?(.*?)[, ]+([A-Z]{2}) +\\d{5}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CFS: ")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    String addr = data.strAddress;
    data.strAddress = "";
    addr = stripFieldStart(addr, "@");
    addr = addr.replace('@', '&').replace("//", "&");
    addr = stripFieldEnd(addr, data.strState);
    addr = stripFieldEnd(addr, data.strCity);
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
    Matcher match = CITY_ST_ZIP_PTN.matcher(data.strApt);
    if (match.matches()) {
      data.strApt = getOptGroup(match.group(1));
      data.strCity = match.group(2);
      data.strState = match.group(3);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("ZIP")) return new BaseZipField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = setGPSLoc(field, data);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS PLACE";
    }
  }

  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        if (data.strPlace.length() == 0) data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }

      // We will fix this later
      data.strAddress = field;
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class BaseZipField extends ZipField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        data.strCity = field;
      } else {
        data.strAddress = stripFieldEnd(data.strAddress, field);
      }
    }
  }

  private static final Pattern INFO_LABEL_PTN = Pattern.compile("([A-Za-z]+): *(.*)");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      boolean suppress = false;
      field = stripFieldStart(field, ",");
      for (String part : field.split(",")) {
        part = part.trim();

        if (part.startsWith("Email sent to ")) suppress = true;
        if (suppress) {
          if (part.endsWith(" for Active 911")) suppress = false;
          continue;
        }

        if (part.startsWith("911 Call Received ")) continue;

        Matcher match = INFO_LABEL_PTN.matcher(part);
        if (match.matches()) {
          String key = match.group(1).toUpperCase();
          String value = match.group(2);
          switch (key) {

          case "NAME":
            if (data.strName.length() == 0) data.strName = cleanWirelessCarrier(value);
            continue;

          case "PHONE":
            if (data.strPhone.length() == 0) data.strPhone = value;
            continue;

          case "CONTACT":
          case "ADDRESS":
            continue;
          }
        }

        if (part.equals("CALLER CREATED")) continue;
        if (part.equals("ALERT:")) continue;

        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE INFO";
    }
  }
}
