package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIPresqueIsleCountyParser extends DispatchOSSIParser {

  public MIPresqueIsleCountyParser() {
    super(CITY_CODES, "PRESQUE ISLE COUNTY", "MI",
          "FYI CALL ( GPS1 GPS2 | ) ADDR! X+? ( CITY X+? | ) INFO/N+? ( SRC X+? NAME PHONE | NAME PHONE ) END");
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
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,3}F[DR]", true);
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

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FRS", "FOREST TWP",
      "WVR", "WAVERLY TWP"
  });

}
