package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIPresqueIsleCountyParser extends DispatchOSSIParser {

  public MIPresqueIsleCountyParser() {
    super(CITY_CODES, "PRESQUE ISLE COUNTY", "MI",
          "FYI FYI? CALL ( GPS1 GPS2 | ) ADDR! ADDR2? X+? ( CITY X+? | ) INFO/N+? ( CITY SRC? X+? NAME PHONE | SRC X+? NAME PHONE | NAME PHONE ) END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD: " + body;
    body = body.replace('\n', ';');
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,3}F[DR]", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d\\d\\.\\d+");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (checkAddress(data.strAddress) == STATUS_STREET_NAME) {
        field = data.strAddress + " & " + field;
        data.strAddress = "";
        super.parse(field, data);
      }
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern CROSS_ST_PTN = Pattern.compile(".* HWY");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (CROSS_ST_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }

  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FRS", "FOREST TWP",
      "WVR", "WAVERLY TWP"
  });

}
