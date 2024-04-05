package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIBarryCountyBParser extends FieldProgramParser {

  public MIBarryCountyBParser() {
    this("BARRY COUNTY", "MI");
  }

  public MIBarryCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "Location:ADDR! ESN:BOX! Cross_Streets:X! ( Call_Details:INFO! Use_Caution:CAUTION! | Use_Caution:CAUTION! Call_Details:INFO! ) " +
          "ProQA:INFO2! All_ProQA:INFO2! Latitude:GPS1 Longitude:GPS2! ( CFS#:ID! | #:ID! ) Units:UNIT? END");
  }

  @Override
  public String getAliasCode() {
    return "MIBarryCountyB";
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Code:")) return false;
    data.strCall = subject.substring(5).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("CAUTION")) return new MyCautionField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([A-Z ]*), *(?!ER)([A-Z]{2})(?: +(\\d{5}))?\\b *");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("(?!ER)([A-Z]{2})(?: +(\\d{5}))?\\b *");
  private static final Pattern APT_PLACE_PTN1 = Pattern.compile("(?:APT|LOT|ROOM|RM) *(\\S+) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PLACE_PTN2 = Pattern.compile("(\\d+)\\b[ /]*(.*)");
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*?) (?:(?:APT|LOT|ROOM|RM) *(\\S+)|(\\d{1,4}[A-Z]?))", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(field.substring(0,pt).trim(), data);
        field = field.substring(pt+1).trim();
        Matcher match = CITY_ST_ZIP_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strCity = match.group(1).trim();
          data.strState = match.group(2);
          if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(3));
          field = field.substring(match.end()).trim();
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
          field = getLeft();
          match = ST_ZIP_PTN.matcher(field);
          if (match.lookingAt()) {
            data.strState = match.group(1);
            String zip = match.group(2);
            if (data.strCity.isEmpty() && zip != null) data.strCity = zip;
            field = field.substring(match.end());
          }
        }
      }
      else {
        parseAddress(StartType.START_ADDR, field, data);
        field = getLeft();
      }

      field = stripFieldStart(field, "None");
      field = stripFieldEnd(field, "None");
      Matcher match = APT_PLACE_PTN1.matcher(field);
      boolean found = match.matches();
      if (!found) {
        match = APT_PLACE_PTN2.matcher(field);
        found = match.matches();
      }
      if (found) {
        data.strApt = append(data.strApt, "-", match.group(1));
        data.strPlace = match.group(2);
      } else if ((match = PLACE_APT_PTN.matcher(field)).matches()) {
        data.strPlace = match.group(1).trim();
        String apt = match.group(2);
        if (apt == null) apt = match.group(3);
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST APT PLACE";
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyCautionField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.toUpperCase().startsWith("Y")) {
        data.strAlert = "CAUTION";
      }
    }
  }

  private static final Pattern DATE_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : DATE_BRK_PTN.split(field)) {
        part = stripFieldStart(part, "None");
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +(?:ProQA: *)?(?=\\d+\\. +)");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // City
      "HASTINGS",

      // Villages
      "FREEPORT",
      "MIDDLEVILLE",
      "NASHVILLE",
      "WOODLAND",

      // Census-Designated Places
      "DELTON",
      "DOWLING",
      "HICKORY CORNERS",

      // Unincorporated communities
      "ASSYRIA",
      "BANFIELD",
      "CLOVERDALE",
      "COATS GROVE",
      "LACEY",
      "MAPLE GROVE",
      "PRAIRIEVILLE",
      "QUIMBY",
      "SCHULTZ",

      // Townships
      "ASSYRIA TWP",
      "BALTIMORE TWP",
      "BARRY TWP",
      "CARLTON TWP",
      "CASTLETON TWP",
      "HASTINGS CHARTER TWP",
      "HOPE TWP",
      "IRVING TWP",
      "JOHNSTOWN TWP",
      "MAPLE GROVE TWP",
      "ORANGEVILLE TWP",
      "PRAIRIEVILLE TWP",
      "RUTLAND CHARTER TWP",
      "THORNAPPLE TWP",
      "WOODLAND TWP",
      "YANKEE SPRINGS TWP",

      // Ionia County

      // Cities
      "BELDING",
      "IONIA",
      "PORTLAND",

      // Villages
      "CLARKSVILLE",
      "HUBBARDSTON",
      "LAKE ODESSA",
      "LYONS",
      "MUIR",
      "PEWAMO",
      "SARANAC",

      // Townships
      "BERLIN TWP",
      "BOSTON TWP",
      "CAMPBELL TWP",
      "DANBY TWP",
      "EASTON TWP",
      "IONIA TWP",
      "KEENE TWP",
      "LYONS TWP",
      "NORTH PLAINS TWP",
      "ODESSA TWP",
      "ORANGE TWP",
      "ORLEANS TWP",
      "OTISCO TWP",
      "PORTLAND TWP",
      "RONALD TWP",
      "SEBEWA TWP"
  };
}
