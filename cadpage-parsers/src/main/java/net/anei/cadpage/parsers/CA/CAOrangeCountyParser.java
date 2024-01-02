package net.anei.cadpage.parsers.CA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAOrangeCountyParser extends FieldProgramParser {

  public CAOrangeCountyParser() {
    super(CITY_CODES, "ORANGE COUNTY", "CA",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! STATE:ST! LATITUDE:GPS1! LONGITUDE:GPS2! ID:ID! PRI:PRI! DATE:DATETIME! TIME:SKIP! MAP:MAP! UNIT:UNIT! APT:APT! CROSS:X! TAC:CH! END");
  }

  @Override
  public String getFilter() {
    return "inotify@ocfa.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{8}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d?:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", ALL");
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "JOHN WAYNE AIRPORT",    "IRVINE"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANA", "ANAHEIM",
      "AVO", "ALISO VIEJO",
      "BPK", "BUENA PARK",
      "CCU", "COTO DE CAZA",
      "EMU", "ORANGE",
      "GGV", "GARDEN GROVE",
      "IRV", "IRVINE",
      "JWA", "JOHN WAYNE AIRPORT",
      "LAF", "LOS ALAMITOS",
      "LDU", "LADERA RANCH",
      "LGH", "LAGUNA HILLS",
      "LGN", "LAGUNA NIGUEL",
      "LGW", "LAGUNA WOODS",
      "LKF", "LAKE FOREST",
      "MCU", "MIDWAY CITY",
      "MVO", "MISSION VIEJO",
      "PLA", "PLACENTIA",
      "RSM", "RANCHO SANTA MARGARITA",
      "SCL", "SAN CLEMENTE",
      "SCP", "SAN JUAN CAPISTRANO",
      "SLB", "SEAL BEACH",
      "STA", "SANTA ANA",
      "STN", "STANTON",
      "TSU", "TUSTIN",
      "WST", "WESTMINSTER",
      "YBL", "YORBA LINDA"
  });
}
