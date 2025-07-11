package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class ALHaynesAmbulanceParser extends DispatchProQAParser {

  public ALHaynesAmbulanceParser() {
    this("");
  }

  ALHaynesAmbulanceParser(String defCounty) {
    super(defCounty, "AL",
          "( ID! ADDR GPS1 GPS2 CITY APT PLACE DEST! INFO/N+? CALL " +
          "| ADDR GPS1 GPS2 CITY APT PLACE  DEST! INFO/N+? CALL ID ) END", true);
  }

  @Override
  public String getLocName() {
    if (getDefaultCity().isEmpty()) {
      return "Haynes Ambulance";
    } else {
      return super.getLocName();
    }
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public String getFilter() {
    return "copier2@haynes-ambulance.com,6245";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("DEST")) return new MyDestinationField();
    if (name.equals("CALL")) return new CallField("[AB]LS", true);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("\\d+% \\d\\d\\.\\d{3}' [NW]");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private class MyDestinationField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      data.strSupp = "Dest: " + field;
    }
  }
}
