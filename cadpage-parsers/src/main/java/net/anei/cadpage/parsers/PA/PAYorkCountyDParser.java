package net.anei.cadpage.parsers.PA;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class PAYorkCountyDParser extends FieldProgramParser {

  public PAYorkCountyDParser() {
    super("YORK COUNTY", "PA",
          "( SELECT/RR Location:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! CFS:CFS! TIMES/N+? " +
          "| DATE_TIME BOX:BOX_CALL! ADDR! CITY! APT_PLACE CROSS_STREETS:X_INFO! UNITS:UNIT UNIT+ )",
          FLDPROG_IGNORE_CASE);
    setupProtectedNames("FISH AND GAME");
  }

  @Override
  public String getFilter() {
    return "york911alert@comcast.net,paging@ycdes.org,paging@zoominternet.net,armstrong1@zoominternet.net,messaging@iamresponding.com,j.bankert712@gmail.com,dtfdfilter@yahoo.com,pager@fairviewems.org,MRKIDD@YCDES.LCL,york911alerts@gmail.com,@active911.com,wrblackwell@comcast.net";
  }


  private HtmlDecoder decoder = null;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<html>") || body.startsWith("<style")) {
      if (decoder == null) decoder = new HtmlDecoder();
      String[] flds = decoder.parseHtml(body);
      if (flds == null) return false;
      setSelectValue("RR");
      data.msgType = MsgType.RUN_REPORT;
      return parseFields(flds, data);
    }

    setSelectValue("");
    return super.parseHtmlMsg(subject, body, data);
  }

  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("Station \\d+");
  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile("[ \n]\\[\\d{4}\\](?:$| *[-\n]| {3})");
  private static final Pattern IAR_PTN1 = Pattern.compile("(?!(?:\\d{7} )?(?:BOX:|box:)).*\n.*\n.*");
  private static final Pattern IAR_PTN2 = Pattern.compile("(?!BOX:|box:)(.*), ([^,]*) :(?: |$)(.*)");
  private static final Pattern BOX_PTN = Pattern.compile("BOX:", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELIM = Pattern.compile(", |(?<!,) +(?=(?:box|cross streets|units):)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (SUBJECT_SRC_PTN.matcher(subject).matches()) data.strSource = subject;

    // Trim trailing garbage
    int pt = body.indexOf('\04');
    if (pt >= 0) body = body.substring(0,pt).trim();
    Matcher match = TRAIL_JUNK_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    // Undo IAR edits :(
    if (IAR_PTN1.matcher(body).lookingAt()) {
      body = "box: " + body.replace('\n', ',');
    } else {
      body = body.replace("=\n", " ").replace('\n', ' ').replace("cross  streets:", "cross streets:");
    }

    if (subject.equals("WMTFD") || subject.equals("41 Wrightsville") || subject.equals("50 W Manchester")) {
      if (!BOX_PTN.matcher(body).lookingAt()) body = "box: " + body;
    }
    else if (subject.equals("Station 68")) {
      match = IAR_PTN2.matcher(body);
      if (match.matches()) {
        body = "box: " + match.group(1) + ", cross streets:" + match.group(2) + " units:" + match.group(3);
      }
    }

    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("DATE_TIME")) return new MyDateTimeField();
    if (name.equals("BOX_CALL")) return new MyBoxCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("X_INFO")) return new MyCrossInfoField();
    if (name.equals("UNIT")) return new MyUnitField();

    if (name.equals("CFS")) return new MyCFSField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{6})?\\b ?\\b(\\d\\d:\\d\\d:\\d\\d)?(?: (\\d\\d-\\d\\d-\\d\\d))?");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strTime = getOptGroup(match.group(2));
        data.strDate = getOptGroup(match.group(3)).replace('-', '/');
      }
    }
  }

  private static final Pattern BOX_CALL_PTN = Pattern.compile("(\\d+-\\d+) +(.*)");
  private static final Pattern CL_PTN = Pattern.compile("(CL \\d)\\b *(.*)");
  private class MyBoxCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = BOX_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strBox = match.group(1);
        field = match.group(2);
      }
      int pt = field.indexOf("  ");
      if (pt >= 0) {
        String place = field.substring(pt+2).trim();
        field = field.substring(0,pt);
        match = CL_PTN.matcher(place);
        if (match.matches()) {
          field = field + " " + match.group(1);
          place = match.group(2);
        }
        data.strPlace = place;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "BOX CALL PLACE";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.equals(field)) data.strPlace = "";
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern CITY_PLACE_PTN = Pattern.compile("[NSEW]B|(?:NORTH|SOUTH|EAST|WEST) OF.*", Pattern.CASE_INSENSITIVE);
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (CITY_PLACE_PTN.matcher(field).matches()) {
        data.strPlace = field;
      } else {
        if (field.toUpperCase().endsWith(" BORO")) field = field.substring(0,field.length()-5).trim();
        super.parse(field, data);
        if (data.strCity.equalsIgnoreCase("BALTIMORE COUNTY")) data.strState = "MD";
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY ST PLACE?";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM) *(.*)|(.*\\d.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = match.group(2);
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern FL_CROSS_PTN = Pattern.compile("(\\d+[A-Za-z]* +FL)\\b *(.*)");

  private class MyCrossInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = FL_CROSS_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        field = match.group(2);
      }
      if (field.toUpperCase().startsWith("NO CROSS STREETS FOUND")) {
        data.strSupp = field.substring(22).trim();
      } else {
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_NO_IMPLIED_APT, field);
        if (res.isValid()) {
          res.getData(data);
          field = res.getLeft();
        }
        data.strSupp = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "APT X INFO";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static final Pattern CFS_PTN = Pattern.compile(".*? Box: (.*?) (Received: .*)");
  private class MyCFSField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CFS_PTN.matcher(field);
      if (!match.matches()) abort();

      data.strBox = match.group(1).trim();
      data.strSupp = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "BOX INFO";
    }
  }

  private class MyTimesField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("SAVE PAPER") || field.startsWith("CONFIDENTIALITY NOTICE")) return false;
      super.parse(field, data);
      return true;
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "CHANCEFORD",           "BROGUE",
      "CODORUS",              "GLENVILLE",
      "CONEWAGO",             "YORK",
      "CROSS ROAD",           "CROSS ROADS",
      "EAST HOPEWELL",        "CROSS ROADS",
      "EAST MANCHESTER",      "MOUNT WOLF",
      "FAIRVIEW",             "NEW CUMBERLAND",
      "FAWN GROVE",           "FAWN GROVE",
      "FAWN",                 "FAWN GROVE",
      "FRANKLIN",             "DILLSBURG",
      "HEIDELBERG",           "PORTERS",
      "HELLAM",               "HALLAM",
      "HOPEWELL",             "STEWARTSTOWN",
      "JACKSON",              "SPRING GOVE",
      "LOWER CHANCEFORD",     "AIRVILLE",
      "LOWER WINDSOR",        "WRIGHTSVILLE",
      "MANHEIM",              "GLENVILLE",
      "MONAGHAN",             "DILLSBURG",
      "MOUNT WOLF",           "MOUNT WOLF",
      "NEWBERRY",             "ETTERS",
      "NORTH CODORUS",        "SPRING GROVE",
      "NORTH HOPEWELL",       "FELTON",
      "NORTH YORK",           "NORTH YORK",
      "PARADISE",             "ABBOTTSTOWN",
      "PEACH BOTTOM",         "DELTA",
      "PENN",                 "HANOVER",
      "SPRING GARDEN",        "YORK",
      "SPRINGETTSBURG",       "YORK",
      "SPRINGFIELD",          "SEVEN VALLEYS",
      "WARRINGTON",           "WELLSVILLE",
      "WASHINGTON",           "EAST BERLIN",
      "WEST MANCHESTER",      "YORK",
      "WEST MANHEIM",         "HANOVER",
      "WEST YORK",            "YORK",
      "YORK CITY",            "YORK",
      "YORK HAVEN",           "YORK HAVEN",
      "YORK",                 "DALLASTOWN",
      "YORKANA",              "YORKANA"
  });
}
