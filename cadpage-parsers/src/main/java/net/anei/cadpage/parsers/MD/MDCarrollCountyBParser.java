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
    if (pt < 0) pt = body.indexOf("\n\nReply STOP");
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
    if (name.equals("BOX")) return new BoxField("\\d{3,}[A-Z]{0,2}|[A-Z]C00", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT_CITY_ST")) return new MyAptCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
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


  private static final Pattern MA_ADDR_PTN1 = Pattern.compile("(?:(\\d{2}-\\d{1,2})[ /]+)?(.*) @ [A-Z]C, *(.*), *([A-Z]{2})");
  private static final Pattern MA_ADDR_PTN2 = Pattern.compile("([A-Z]C), *(?:(\\d{2}-\\d{1,2})[ /]+)?(.*)");
  private static final Pattern MA_ADDR_DELIM = Pattern.compile(" *(?<! W)/+ *");
  private static final Pattern INTERSECT_DBL_SFX_PTN = Pattern.compile("(.*?) */ *(.*) +(AVE|BLVD|DR|LN|RD|ST) +(AVE|BLVD|DR|LN|RD|ST)");
  private class MyAddressField extends Field {

    @Override
    public void parse(String fld, Data data) {

      // Mutual aid calls have their own unique address structure
      if (data.strCall.startsWith("MUTUAL AID")) {

        String box = null;
        String addr = null;;
        Matcher match = MA_ADDR_PTN1.matcher(fld);
        if (match.matches()) {
          box = match.group(1);
          addr = match.group(2).trim();
          data.strCity = match.group(3);
          data.strState = match.group(4);
        }

        else if ((match = MA_ADDR_PTN2.matcher(fld)).matches()) {
          String code = match.group(1);
          box = match.group(2);
          addr = match.group(3);
          String city = COUNTY_CODES.getProperty(code);
          if (city == null) abort();
          int pt = city.indexOf(',');
          if (pt >= 0) {
            data.strState = city.substring(pt+1);
            city = city.substring(0,pt);
          }
          data.strCity = city;
        }

        if (addr != null) {
          if (box != null) data.strBox = box;
          String[] parts = MA_ADDR_DELIM.split(addr);
          int ndx = 0;
          addr = parts[ndx++];
          while (ndx < parts.length-2) {
            addr = append(addr, " & ", parts[ndx++]);
          }
          if (ndx < parts.length) {
            data.strCall = data.strCall = append(data.strCall, " - ", parts[ndx++]);
            if (ndx < parts.length) data.strChannel = parts[ndx++];
          }

          parseAddress(StartType.START_ADDR, addr, data);
          data.strPlace = getLeft();
          return;
        }
      }

      // resume normal address parsing

      // Strip off leading place
      Parser p = new Parser(fld);
      data.strPlace = p.getOptional('@');

      // Strip off state and city
      String city = p.getLastOptional(',');
      if (city.length() == 2) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;

      // ANd maybe a trailing place
      data.strPlace = append(data.strPlace, " - ", p.getLastOptional(','));

      fld = p.get();

      // They do weird things with intersection street names
      Matcher match = INTERSECT_DBL_SFX_PTN.matcher(fld);
      if (match.matches()) {
        fld = match.group(1) + ' ' + match.group(3) + " & " + match.group(2) + ' ' + match.group(4);
      }

      parseAddress(fld, data);
    }

    @Override
    public String getFieldNames() {
      return "BOX ADDR APT PLACE CALL CH CITY ST";
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
