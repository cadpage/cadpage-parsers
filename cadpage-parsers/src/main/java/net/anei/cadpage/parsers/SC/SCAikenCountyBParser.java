package net.anei.cadpage.parsers.SC;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCAikenCountyBParser extends FieldProgramParser {

  public SCAikenCountyBParser() {
    super(CITY_LIST, "AIKEN COUNTY", "SC",
          "NOTICE:CALL! ( ADDRESS:SKIP! DATETIME1! LOC_INFO:ADDR! " +
                       "| ID:ID! ADDRESS:ADDR_X! CITY:CITY! RECEIVED:DATETIME2! LOC_INFO%EMPTY! PLACE:PLACE! " +
                       ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@northaugustasc.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CALL NOTICE")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new DateTimeField("RECEIVED +AT +(\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d)", true);
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR_X")) return new MyAddressCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_DELIM_PTN = Pattern.compile(" *((?:\\bX2?)?\\[|\\]) *", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_DELIM_PTN.matcher(field);
      String delim = null;
      int ipt = 0;
      while (match.find()) {
        parsePart(delim, field.substring(ipt, match.start()), false, data);
        delim = match.group(1);
        ipt = match.end();
      }
      parsePart(delim, field.substring(ipt), true, data);

      String addr = data.strAddress;
      data.strAddress = "";
      parseAddress(addr, data);
    }

    private void parsePart(String delim, String fld, boolean last, Data data) {
      if (delim == null) {
        data.strAddress = fld;
      } else if (delim.length() > 1) {
        data.strCross = append(data.strCross, " / ", fld);
      } else if (data.strCity.isEmpty() && isCity(fld)) {
        data.strCity = fld;
      } else {
        if (last && data.strCity.isEmpty()) {
          String city = CITY_SET.tailSet(fld).first();
          if (city != null) {
            data.strCity = city;
            return;
          }
        }
        if (!fld.equals(data.strAddress)) {
          data.strPlace = append(data.strPlace, " - ", fld);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE X CITY";
    }
  }

  private static final Pattern ADDR_X_PTN = Pattern.compile("(.*?) *\\bx\\b *(.*)");

  private class MyAddressCrossField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_X_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCross = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AIKEN",
      "NEW ELLENTON",
      "NORTH AUGUSTA",

      // Towns
      "BURNETTOWN",
      "JACKSON",
      "PERRY",
      "SALLEY",
      "MONETTA",
      "WAGENER",
      "WINDSOR",

      // Census-designated places
      "BEECH ISLAND",
      "BELVEDERE",
      "CLEARWATER",
      "GLOVERVILLE",
      "GRANITEVILLE",
      "LANGLEY",
      "WARRENVILLE",

      // Unincorporated communities
      "BATH",
      "EUREKA",
      "NEW HOLLAND",
      "SEIVERN",
      "SPIDERWEB",
      "TALATHA",
      "WHITE POND",
      "VAUCLUSE"
  };

  private static final TreeSet<String> CITY_SET = new TreeSet<>(Arrays.asList(CITY_LIST));
}
