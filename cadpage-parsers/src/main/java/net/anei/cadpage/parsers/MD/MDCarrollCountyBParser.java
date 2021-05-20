package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Carroll County, MD
 */
public class MDCarrollCountyBParser extends FieldProgramParser {

  private static final Pattern SUBJECT_PTN = Pattern.compile("Station (\\d\\d) ALERT!! \\(#?(E?F?\\d+)\\)");

  public MDCarrollCountyBParser() {
    super("CARROLL COUNTY", "MD",
           "CALL ( BOX ( UNIT ADDR! | ADDR! UNIT ) | UNIT BOX ADDR! APT:APT_CITY_ST? ) INFO+");
    setupMultiWordStreets(
        "BUTLERS BRANCH",
        "CHRIS MAR",
        "CARROLL CREEK VIEW",
        "COON CLUB",
        "EDGEWOOD CHURCH",
        "FORT FOOTE",
        "MIKE SHAPIRO",
        "MT GILEAD",
        "OLD ALEXANDRIA FERRY",
        "RUSTIC VIEW"
    );
  }

  @Override
  public String getFilter() {
    return "FASTalert System,.fastalerting.com,Volunteer25@co.pg.md.us,fast@fastalerting.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;

    int pt = body.indexOf("\n\n___");
    if (pt >= 0) body = body.substring(0,pt).trim();

    data.strSource = match.group(1);
    data.strCallId = match.group(2);
    body = body.replaceAll("  +", " ");
    body = body.replace("\n,", ",");
    return parseFields(body.split("\n"), 3, data);
  }

  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT_CITY_ST")) return new MyAptCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  // Box field behaves normally unless this is a mutual aid call
  // in which case it becomes a county code
  private static final Pattern BOX_PTN = Pattern.compile("\\d{3,}[A-Z]{0,2}");
  private class MyBoxField extends BoxField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCall.startsWith("MUTUAL AID")) {
        parseMACityCode(field, data);
        return true;
      }

      if (field.length() == 4 && field.startsWith("MA")) {
        if (!data.strCall.startsWith("MUTUAL AID")) {
          data.strCall = "MUTUAL AID " + data.strCall;
        }
        parseMACityCode(field.substring(2), data);
        return true;
      }

      if (BOX_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }
      return false;
    }

    private void parseMACityCode(String field, Data data) {
      String[] tmp = convertCodes(field, COUNTY_CODES).split(",");
      data.strCity = tmp[0];
      if (tmp.length > 1) data.strState = tmp[1];
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAptCityStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (STATE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      String apt = p.get();
      if (apt.isEmpty()) {
        int pt = city.indexOf(' ');
        if (pt >= 0) {
          apt = city.substring(0,pt).trim();
          city = city.substring(pt+1).trim();
        } else {
          apt = city;
          city = "";
        }
      }
      data.strApt = apt;
      data.strCity = city;
    }

    @Override
    public String getFieldNames() {
      return "APT CITY ST";
    }
  }

  private static final Pattern UNIT_MULT_SPC_PTN = Pattern.compile("  +");
  private static final String SINGLE_UNIT_PTN_SPC = "(?:[A-Z]+\\d+|\\d[A-Z]+)";
  private static final String MULTI_UNIT_PTN_SPC = SINGLE_UNIT_PTN_SPC + "(?: +" + SINGLE_UNIT_PTN_SPC + ")*";
  private class MyUnitField extends UnitField {

    public MyUnitField() {
      super(MULTI_UNIT_PTN_SPC);
    }

    @Override
    public void parse(String field, Data data) {
      field = UNIT_MULT_SPC_PTN.matcher(field).replaceAll(" ");
      super.parse(field,  data);
    }
  }


  private static final Pattern ADDR_AT_AT_PTN = Pattern.compile("(.*? AT .*?) at (.* AT .*)");
  private static final Pattern ADDR_BOX_PTN = Pattern.compile("\\d{2}-\\d{1,2}");
  private static final Pattern APT_PTN = Pattern.compile("(?:\\bAPT\\b|\\bROOM\\b|\\bRM\\b|\\bUNIT\\b|#) *([^ ]+)$");
  private static final Pattern APT_PTN2 = Pattern.compile("(?:\\bAPT(?![A-Z])|\\bROOM|\\bRM|\\bUNIT\\b|#) *([^ ]+) *");
  private static final Pattern CHANNEL_PTN = Pattern.compile(" TG *(.*)$");
  private static final Pattern SEPARATOR = Pattern.compile(";| // ");
  private class MyAddressField extends Field {

    @Override
    public void parse(String fld, Data data) {

      // Rules are completely different for old style mutual aid calls
      // If it was, there may be a box code,
      // there is some addition call description stuff following the address
      // And the last token is a radio code

      // New style mutual aid call address contains a comma and should be
      // treated normally
      if (!fld.contains(",") && data.strCall.startsWith("MUTUAL AID")) {
        Parser p = new Parser(fld);
        String tmp = p.get(' ');
        if (tmp.equals("BOX") || tmp.equals("BC")) {
          data.strBox = p.get(' ');
          fld = p.get();
        } else if (ADDR_BOX_PTN.matcher(tmp).matches()) {
          data.strBox = tmp;
          fld = p.get();
        }
        Matcher match = CHANNEL_PTN.matcher(fld);
        if (match.find()) {
          data.strChannel = match.group(1).trim();
          fld = fld.substring(0,match.start()).trim();
        }

        String call;
        match = SEPARATOR.matcher(fld);
        if (match.find()) {
          parseAddress(fld.substring(0,match.start()).trim(), data);
          int pt = match.end();
          if (data.strChannel.length() == 0 && match.find()) {
            call = fld.substring(pt, match.start()).trim();
            data.strChannel = fld.substring(match.end()).trim();
          } else {
            call = fld.substring(pt).trim();
          }
        } else {
          parseAddress(StartType.START_ADDR, fld, data);
          call = getLeft();
        }
        data.strCall = append(data.strCall, " - ", call);
      }

      // resume normal address parsing
      else {
        // Strip off state and city
        Parser p = new Parser(fld);
        String city = p.getLastOptional(',');
        if (city.length() == 2) {
          if (!city.equals(data.defState)) data.strState = city;
          city = p.getLastOptional(',');
        }
        data.strCity = city;
        fld = p.get();

        // Strip off any cross street numbers
        if (fld.endsWith(">")) {
          int pt = fld.indexOf('<');
          if (pt >= 0) {
            fld = fld.substring(0,pt).trim();
            fld = stripFieldEnd(fld, "#");
          }
        }

        // They have a weird of of designating intersections that we will try to simplify
        Matcher match = ADDR_AT_AT_PTN.matcher(fld);
        if (match.matches()) {
          String p1 = match.group(1).trim();
          String p2 = match.group(2).trim();
          if (p1.equals(p2)) fld = p1;
        }

        // See if we can find an apt field
        match = APT_PTN.matcher(fld);
        if (match.find()) {
          data.strApt = match.group(1);
          fld = fld.substring(0,match.start()).trim();
        }


        // Rest of address could include a place name separated by a ; or @
        // Unfortunately, the two fields might be in either order :(
        if (fld.startsWith("@")) fld = fld.substring(1).trim();
        int pt = fld.indexOf('@');
        if (pt < 0) pt = fld.indexOf(';');
        if (pt < 0) {
          match = APT_PTN2.matcher(fld);
          if (match.find()) {
            data.strApt = match.group(1);
            data.strPlace = fld.substring(0,match.start()).trim();
            fld = fld.substring(match.end()).trim();
          }
          if (data.strPlace.length() > 0) {
            parseAddress(fld, data);
          } else {
            parseAddress(StartType.START_PLACE, FLAG_IGNORE_AT | FLAG_ANCHOR_END, fld, data);
            if (data.strAddress.length() == 0) {
              parseAddress(data.strPlace, data);
              data.strPlace = "";
            }
            else if (data.strPlace.length() > 0) {
              if (data.strApt.length() > 0) {
                data.strApt = data.strApt + ' ' + data.strPlace;
                data.strPlace = "";
              }
            }
          }
        }
        else {
          String fld1 = fld.substring(0,pt).trim();
          String fld2 = fld.substring(pt+1).trim();
          Result res1 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_IGNORE_AT | FLAG_ANCHOR_END, fld1);
          Result res2 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_IGNORE_AT | FLAG_ANCHOR_END, fld2);
          if (res2.getStatus() > res1.getStatus()) {
            res1 = res2;
            fld2 = fld1;
          }
          res1.getData(data);
          data.strPlace = fld2;
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "BOX PLACE ADDR APT CITY ST CH";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Darea:")) {
        Parser p = new Parser(field.substring(6).trim());
        data.strChannel = p.get(" - ");
        field = p.get();
      }
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = ADDR_AT_RP_PTN.matcher(addr);
    if (match.matches()) {
      String p3 = match.group(3);
      if (p3 != null) {
        addr = match.group(2).trim() + " & " + p3.trim();
      } else {
        addr = match.group(1).trim() + " & " + match.group(2).trim();
      }
    }
    return addr;
  }
  private static final Pattern ADDR_AT_RP_PTN = Pattern.compile("(.*?) AT (.*?)(?: RP (.*))?");

  // Mutual aid count abbreviations
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "YC", "YORK COUNTY,PA",
      "BC", "BALTIMORE COUNTY",
      "CH", "CHARLES COUNTY",
      "HC", "HOWARD COUNTY",
      "FC", "FREDERICK COUNTY",
      "AC", "ADAMS COUNTY,PA",
      "MC", "MONTGOMERY COUNTY"
  });
}
