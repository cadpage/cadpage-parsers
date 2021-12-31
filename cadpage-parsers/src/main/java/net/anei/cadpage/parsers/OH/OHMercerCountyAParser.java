package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMercerCountyAParser extends FieldProgramParser {

  public OHMercerCountyAParser() {
    super("MERCER COUNTY", "OH",
          "SRC CALL STATUS? ADDRCITY UNIT BOX! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,4}", true);
    if (name.equals("STATUS")) return new SkipField("DIS|ENR");
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]{3,5}\\d?|SQ\\d{1,2}", true);
    if (name.equals("BOX")) return new BoxField("\\d{4}");
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("APT +(.*)|\\d{1,4}[A-Z]?|[A-Z]");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
        field = field.substring(0, pt).trim();
      }
      for (String part : field.split(";")) {
        part = part.trim();
        if (part.length() == 0) continue;
        if (data.strAddress.length() == 0) {
          parseAddress(field, data);
        } else {
          Matcher match = APT_PTN.matcher(part);
          if (match.matches()) {
            String apt = match.group(1);
            if (apt == null) apt = part;
            if (!data.strApt.contains(apt)) {
              data.strApt = append(data.strApt, "-", apt);
            }
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANN",  "ANNA",
      "ANS",  "ANSONIA",
      "ARC",  "ARCANUM",
      "BAT",  "BATAVIA",
      "BEL",  "BELLEFONTAINE",
      "BER",  "BERNE",
      "BOT",  "BOTKINS",
      "BOW",  "BOWLING GREEN",
      "BRY",  "BRYANT",
      "BUR",  "BURKETTSVILLE",
      "BYA",  "BRYAN",
      "CEL",  "CELINA",
      "CHI",  "CHICKASAW",
      "CIN",  "CINCINNATI",
      "CON",  "CONVOY",
      "CRV",  "CRIDERSVILLE",
      "CW",  "COLDWATER",
      "DAY",  "DAYTON",
      "DEF",  "DEFIANCE",
      "DEL",  "DELPHOS",
      "EAT",  "EATON",
      "FIN",  "FINDLAY",
      "FKX",  "FORT KNOX",
      "FN",  "FINDLAY",
      "FRE",  "FREMONT",
      "FTL",  "FORT LORAMIE",
      "FTR",  "FORT RECOVERY",
      "GAL",  "GALLIPOLIS",
      "GEO",  "GEORGETOWN",
      "GRE",  "GREENVILLE",
      "HAM",  "HAMILTON",
      "HIL",  "HILLSBORO",
      "IRO",  "IRONTON",
      "KEN",  "KENTON",
      "LEB",  "LEBANON",
      "LIM",  "LIMA",
      "LON",  "LONDON",
      "MAR",  "MARIA STEIN",
      "MEN",  "MENDON",
      "MIS",  "MINSTER",
      "MON",  "MONTEZUMA",
      "MYL",  "MARYSVILLE",
      "NAP",  "NAPOLEON",
      "NEB",  "NEW BREMEN",
      "NEW",  "NEW WESTON",
      "NKX",  "NEW KNOXVILLE",
      "NMD",  "NEW MADISON",
      "NOR",  "NORTH STAR",
      "OHC",  "OHIO CITY",
      "ORI",  "ORIENT",
      "OSG",  "OSGOOD",
      "OTT",  "OTTAWA",
      "PAU",  "PAULDING",
      "PIQ",  "PIQUA",
      "ROC",  "ROCKFORD",
      "ROS",  "ROSSBURG",
      "RUS",  "RUSSIA",
      "SID",  "SIDNEY",
      "SPE",  "SPENCERVILLE",
      "SPR",  "SPRINGFIELD",
      "STH",  "ST HENRY",
      "STM",  "ST MARYS",
      "TOL",  "TOLEDO",
      "TRO",  "TROY",
      "UCI",  "UNION CITY",
      "UCO",  "UNION CITY",
      "URB",  "URBANA",
      "VEN",  "VENEDOCIA",
      "VER",  "VERSAILLES",
      "VWT",  "VAN WERT",
      "WAP",  "WAPAKONETA",
      "WAS",  "WASHINGTON C.H.",
      "WAU",  "WAUSEON",
      "WAV",  "WAVERLY",
      "WIL",  "WILLSHIRE",
      "WIN",  "WINCHESTER",
      "WLM",  "WILMINGTON",
      "WOO",  "WOOSTER",
      "WPD",  "WELLSTON",
      "WUN",  "WEST UNION",
      "XEN",  "XENIA",
      "YOR",  "YORKSHIRE",

  });
}
