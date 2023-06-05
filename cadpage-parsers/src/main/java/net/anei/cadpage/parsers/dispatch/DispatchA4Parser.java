package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA4Parser extends FieldProgramParser {

  private static final Pattern TRAIL_AND_PTN = Pattern.compile("(.*)(?:\\bAND|/|&)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SLASH_PTN = Pattern.compile("/+");

  public DispatchA4Parser(String defCity, String defState) {
    this(defCity, defState, 1);
  }

  public DispatchA4Parser(String defCity, String defState, int version) {
    super(defCity, defState,
          "( SELECT/NEW CALL ADDR! APT | " +
          (version == 2 ? "CALL! EMPTY! CITY! ADDR/SP! Apt:APT! Cross_Streets:X"
                        : "CALL! ADDR1! Apt:APT! CITY GPS? PLACE Cross_Streets:X") + " ) ");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Page for CFS ")) return false;
    data.strCallId = subject.substring(17).trim();

    if (!body.contains("\n")) {
      setSelectValue("NEW");
      return parseFields(body.split(","), data);
    }

    setSelectValue("OLD");
    if (! parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.length() == 0) {
      data.strAddress = data.strPlace;
      data.strPlace = "";
    } else {
      Matcher match = TRAIL_AND_PTN.matcher(data.strPlace);
      if (match.matches()) {
        data.strAddress = append(match.group(1).trim(), " & ", data.strAddress);
        data.strPlace = "";
      }
    }
    if (SLASH_PTN.matcher(data.strApt).matches()) data.strApt = "";
    if (SLASH_PTN.matcher(data.strCity).matches()) data.strCity = "";
    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      // Stuff following the comma is (apparently) always duplicated
      // in the city or building fields, so we just ignore it
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('*', '/');
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("http://maps\\.google\\.com.*=([-+]?\\d+\\.\\d{5,})(?:\\%20|[ ,])([-+]?\\d+\\.\\d{5,})");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace(" ", "");
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        return true;
      }
      if (field.startsWith("http://maps.google.com")) return true;
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
