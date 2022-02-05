package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.ReverseCodeTable;


public class OHHamiltonCountyAParser extends FieldProgramParser {

  public OHHamiltonCountyAParser() {
    super("HAMILTON COUNTY", "OH",
          "CALL! CH ADDR! Bld:APT! Apt:APT! PLACE SRC TIME UNIT! NAME Xst:X! INFO/N+? GPS1/d GPS2/d");
  }

  @Override
  public String getFilter() {
    return "hc@hamilton-co.org,9300,messaging@iamresponding.com,active911@lsfd.org,6245";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  public String adjustMapAddress(String addr) {
    return WILLIAM_HENRY_HARR.matcher(addr).replaceAll("WILLIAM HENRY HARRISON");
  }
  private static final Pattern WILLIAM_HENRY_HARR = Pattern.compile("\\bWILLIAM HENRY HARR\\b", Pattern.CASE_INSENSITIVE);

  private static final Pattern PREFIX_PTN = Pattern.compile("(?:HC|CAD|DIRECT):");
  private static final Pattern CALL_PFX_PTN = Pattern.compile("(2ND CALL) *(.*)");

  @Override
  public boolean parseMsg(String body, Data data) {

    if (body.startsWith("HC:AD:") || body.startsWith("HC:TIME:")) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.substring(3);
      return true;
    }

    String[] flds = body.split(">");
    if (flds.length < 5) return false;
    Matcher match = PREFIX_PTN.matcher(flds[0]);
    if (match.lookingAt()) flds[0] = flds[0].substring(match.end()).trim();

    String prefix = "";
    match = CALL_PFX_PTN.matcher(flds[0]);
    if (match.matches()) {
      prefix = match.group(1);
      flds[0] = match.group(2);
    }
    if (!parseFields(flds, data)) return false;
    data.strCall = append(prefix, " ", data.strCall);

    String city = DEPT_CITY_TABLE.getCodeDescription(data.strSource);
    if (city != null) data.strCity = city;
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("SRC", "SRC CITY");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("(?:\\b(?:[A-Z]{2,3}\\d{1,2}|NECC)\\b ?)*", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS1")) return new GPSField(1, "\\d{8,9}");
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("1) ")) return;
      int pt = field.indexOf(" 1) ");
      if (pt >= 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *,?\\[\\d{1,2}\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final ReverseCodeTable DEPT_CITY_TABLE = new ReverseCodeTable(
      "Amberly Village FD",         "Amberley Village",
      "Amberley Villag",            "Amberley Village",
      "Anderson Twp FD",            "Cincinnati",
      "Blue Ash FD",                "Blue Ash",
      "Cheviot FD",                 "Cheviot",
      "Cincinnati FD",              "Cincinnati",
      "Clermont CO FD",             "Clermont County",
      "Cleves FD",                  "Cleves",
      "Colerain Twp FD",            "Colerain Twp",
      "Crosby Twp FD",              "Crosby Twp",
      "Deerpark/Silverton FD",      "Deer Park",
      "Deerpark-Silver",            "Deer Park",
      "Delhi Twp FD",               "Delhi Twp",
      "Elmwood F",                  "Elmwood",
      "Elmwood FD",                 "Elmwood",
      "Elmwood Place F",            "Elmwood Place",
      "Evendale FD",                "Evendale",
      "Fairfield FD",               "Fairfield",
      "Forest Park FD",             "Forest Park",
      "Glendale FD",                "Glendale",
      "Golf Manor FD",              "Golf Manor",
      "Green Twp FD",               "Green Twp",
      "Greenhills FD",              "Greenhills",
      "Harrison FD",                "Harrison Twp",
      "Lincoln Heights",            "Lincoln Heights",
      "Lincoln Heights FD",         "Lincoln Heights",
      "Little Miami JFD",           "Fairfax",
      "Little Miami JF",            "Fairfax",
      "Lockland FD",                "Lockland",
      "Madeira/Indian Hill JFD",    "Madeira",
      "Madeira-Indian",             "Madeira",
      "Mariemont FD",               "Mariemont",
      "Miami Twp FD",               "North Bend",
      "Milford FD",                 "Milford",
      "Montgomery FD",              "Montgomery",
      "Mt Healthy FD",              "Mt Healthy",
      "N College Hill",             "N College Hill",
      "N College Hill FD",          "N College Hill",
      "Norwood FD",                 "Norwood",
      "Reading FD",                 "Reading",
      "Sharonville FD",             "Sharonville",
      "Springdale FD",              "Springdale",
      "Springfield Twp FD",         "Springfield Twp",
      "Springfield Twp",            "Springfield Twp",
      "St Bernard FD",              "St Bernard",
      "Sycamore Twp FD",            "Sycamore Twp",
      "Sycamore Twp",               "Sycamore Twp",
      "Terrace Park FD",            "Terrace Park",
      "Union Twp FD",               "Union Twp",
      "Warren CO FD",               "Warren County",
      "West Chester FD",            "West Chester",
      "Whitewater Twp FD",          "Whitewater Twp",
      "Whitewater Twp",             "Whitewater Twp",
      "Woodlawn FD",                "Woodlawn",
      "Wyoming FD",                 "Wyoming"
  );
}
