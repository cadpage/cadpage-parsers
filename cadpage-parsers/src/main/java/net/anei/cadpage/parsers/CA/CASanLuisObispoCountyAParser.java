package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * San Luis Obispo County, CA
 */
public class CASanLuisObispoCountyAParser extends FieldProgramParser {

  public CASanLuisObispoCountyAParser() {
    super("SAN LUIS OBISPO COUNTY", "CA",
          "( RA:SKIP! ADDRCITY X CALL UNK! Map:MAP! ID UNIT! INFO/N+? GPS END " +
          "| ID CALL ADDRCITY! ( SELECT/RR INFO! INFO/N+ | COMMAND:CH! Tac:CH/SLS! ATG:CH/SLS! Resources:UNIT! REMARKS:INFO! INFO/N+? GPS END ) )");
  }

  @Override
  public String getFilter() {
    return "slucad@fire.ca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("[;\n]+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    setSelectValue("");
    if (body.startsWith("Close: ")) {
      setSelectValue("RR");
      data.msgType = MsgType.RUN_REPORT;
      body = body.substring(7).trim();
    }

    body = body.replace(" <a href=", "\n<a href=");
    return super.parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("Inc# *(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS")) return new GPSField("<a href=\"http://maps.google.com/\\?q=([-+0-9\\.,]+)\">Map</a>", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      data.strApt = p.getLastOptional('#');
      data.strPlace = p.getOptional('@');
      parseAddress(p.get(), data);
      int pt = city.indexOf('(');
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", stripFieldEnd(city.substring(pt+1).trim(), ")"));
        city = city.substring(0, pt).trim();
      }
      data.strCity = convertCodes(city, CITY_CODES);
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("COPY\\b.*|-{3,} External Remarks.*-{3,}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ATAS",     "ATASCADERO",
      "CMB",      "CAMBRIA",
      "LOS_OSOS", "LOS OSOS",
      "MB",       "MORRO BAY",
      "PR",       "PASO ROBLES",
      "SANTA_MARG","SANTA MARGARITA",
      "SAN_MIG",  "SAN MIGUEL",
      "SL",       "SAN LUIS OBISPO",
      "SLO_CO",   "SAN LUIS OBISPO COUNTY",
      "TEMP",     "TEMPLETON",

  });
}
