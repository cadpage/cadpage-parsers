package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHHamiltonCountyBParser extends FieldProgramParser {

  public OHHamiltonCountyBParser() {
    super("HAMILTON COUNTY", "OH",
          "DATETIME CALL! ADDR Apt:APT? CITY? INFO/N+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "norwoodpolice@norwoodpolice.org,pager@amberleyvillage.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern COMMA_DATE_PTN = Pattern.compile(",(?= *\\d{4}/\\d\\d/\\d\\d \\d\\d:\\d\\d\\b)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Dispatched")) return false;

    String[] flds;
    body = COMMA_DATE_PTN.matcher(body).replaceAll("\n");
    int pt = body.indexOf('\n');
    if (pt < 0) {
      flds = body.split(",");
    } else {
      String[] flds1 = body.substring(0,pt).trim().split(",");
      String[] flds2 = body.substring(pt+1).trim().split("\n");
      flds = new String[flds1.length + flds2.length];
      System.arraycopy(flds1, 0, flds, 0, flds1.length);
      System.arraycopy(flds2, 0, flds, flds1.length, flds2.length);
    }

    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("CITY")) return new CityField("[A-Za-z ]*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_DATE_TIME = Pattern.compile("\\d{4}/\\d\\d/\\d\\d \\d\\d:\\d\\d\\b *(?:[A-Z][a-z]+ *, [A-Z][a-z]+\\b *(?:D\\d+|5J[A-Z0-9]+\\b)?)?[ :]*");
  private static final Pattern INFO_UNIT_PTN = Pattern.compile("( *\\b[A-Z]+\\d+)+$");
  private static final Pattern INFO_ID_PTN = Pattern.compile("[ \\.]*\\bRMS INCIDENT #(\\d+)$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = INFO_DATE_TIME.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());

      match = INFO_UNIT_PTN.matcher(field);
      if (match.find()) {
        data.strUnit = match.group().trim();
        field = field.substring(0,match.start()).trim();
      }

      match = INFO_ID_PTN.matcher(field);
      if (match.find()) {
        data.strCallId = match.group(1);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO ID UNIT";
    }
  }

  private static final Pattern GPS_BOX_PTN1 = Pattern.compile("\\b\\d+ (?:IR71|SR562)\\b");
  private static final Pattern GPS_BOX_PTN2 = Pattern.compile("\\bIR\\d\\d|SR\\d\\d\\b");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    Matcher match = GPS_BOX_PTN1.matcher(address);
    if (match.find()) {
      address = match.group();
    } else {
      match = GPS_BOX_PTN2.matcher(address);
      if (match.find()) address = match.group();
    }
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "IR60",                                 "+39.145352,-84.455978",
      "IR61",                                 "+39.146120,-84.454971",
      "IR62",                                 "+39.146785,-84.453336",
      "IR63",                                 "+39.147337,-84.451525",
      "IR64",                                 "+39.147859,-84.450050",
      "IR65",                                 "+39.148481,-84.448419",
      "IR66",                                 "+39.149239,-84.446617",
      "IR67",                                 "+39.150066,-84.444915",
      "IR68",                                 "+39.151252,-84.443814",
      "IR69",                                 "+39.152564,-84.442976",
      "IR70",                                 "+39.153716,-84.442255",
      "IR72",                                 "+39.156040,-84.440075",
      "IR73",                                 "+39.157445,-84.439015",
      "IR74",                                 "+39.158576,-84.438083",
      "IR75",                                 "+39.159552,-84.437447",
      "IR76",                                 "+39.161183,-84.436577",
      "IR77",                                 "+39.162459,-84.435166",
      "SR12",                                 "+39.168125,-84.466254",
      "SR13",                                 "+39.166470,-84.463158",
      "SR14",                                 "+39.165676,-84.459239",
      "SR15",                                 "+39.165134,-84.457370",
      "SR16",                                 "+39.164662,-84.455382",
      "SR17",                                 "+39.164302,-84.453570",
      "SR18",                                 "+39.163941,-84.451839",
      "SR19",                                 "+39.163861,-84.449986",
      "SR20",                                 "+39.163400,-84.448241",
      "SR21",                                 "+39.163146,-84.445872",
      "SR22",                                 "+39.163177,-84.444402",
      "SR23",                                 "+39.163626,-84.442203",
      "SR24",                                 "+39.164192,-84.440953",
      "SR25",                                 "+39.164550,-84.439354",
      "SR26",                                 "+39.164293,-84.437196",

      "60 IR71",                              "+39.145352,-84.455978",
      "61 IR71",                              "+39.146120,-84.454971",
      "62 IR71",                              "+39.146785,-84.453336",
      "63 IR71",                              "+39.147337,-84.451525",
      "64 IR71",                              "+39.147859,-84.450050",
      "65 IR71",                              "+39.148481,-84.448419",
      "66 IR71",                              "+39.149239,-84.446617",
      "67 IR71",                              "+39.150066,-84.444915",
      "68 IR71",                              "+39.151252,-84.443814",
      "69 IR71",                              "+39.152564,-84.442976",
      "70 IR71",                              "+39.153716,-84.442255",
      "71 IR71",                              "+39.155062,-84.441185",
      "72 IR71",                              "+39.156040,-84.440075",
      "73 IR71",                              "+39.157445,-84.439015",
      "74 IR71",                              "+39.158576,-84.438083",
      "75 IR71",                              "+39.159552,-84.437447",
      "76 IR71",                              "+39.161183,-84.436577",
      "77 IR71",                              "+39.162459,-84.435166",

      "12 SR562",                             "+39.168125,-84.466254",
      "13 SR562",                             "+39.166470,-84.463158",
      "14 SR562",                             "+39.165676,-84.459239",
      "15 SR562",                             "+39.165134,-84.457370",
      "16 SR562",                             "+39.164662,-84.455382",
      "17 SR562",                             "+39.164302,-84.453570",
      "18 SR562",                             "+39.163941,-84.451839",
      "19 SR562",                             "+39.163861,-84.449986",
      "20 SR562",                             "+39.163400,-84.448241",
      "21 SR562",                             "+39.163146,-84.445872",
      "22 SR562",                             "+39.163177,-84.444402",
      "23 SR562",                             "+39.163626,-84.442203",
      "24 SR562",                             "+39.164192,-84.440953",
      "25 SR562",                             "+39.164550,-84.439354",
      "26 SR562",                             "+39.164293,-84.437196"

  });
}
