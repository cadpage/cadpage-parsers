package net.anei.cadpage.parsers.NC;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCCabarrusCountyAParser extends DispatchOSSIParser {

  public NCCabarrusCountyAParser() {
    super(NCCabarrusCountyParser.CITY_CODES, "CABARRUS COUNTY", "NC",
        "( CANCEL ADDR CITY " +
        "| FYI? ( ADDR/Z CITY ID CALL " +
               "| CALL ADDR CITY? X_PLACE+? ( ID | CITY SRC? UNIT? ID? | SRC UNIT? ID? | UNIT SRC? ID? ) " +
               ") " +
        ") INFO+");
    setupCityValues(NCCabarrusCountyParser.CITY_CODES);
    setupCities(OCC_CITY_LIST);
    setupGpsLookupTable(NCCabarrusCountyParser.GPS_LOOKUP_TABLE);
    removeWords("NEW");
  }

  @Override
  public String getFilter() {
    return "CAD@cabarruscounty.us, 93001";
  }

  private static final Pattern STANLEY_CALL_PTN = Pattern.compile("CAD:\\d{8};");
  private static final Pattern SPEC_UNIT_PTN = Pattern.compile("CAD: *\\{([A-Z0-9]+)\\} *");
  private static final Pattern OPER_PTN = Pattern.compile("^\\d+:");
  private static final Pattern MISSING_SEMI_PTN = Pattern.compile("(?<!;)(?=\\(S\\) \\(N\\))");
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+[0-9]+|[A-Z]{2}(?:POV)?|\\d{1,4}|[A-Z]{1,3}PD|FORE|FMO|NECA");

  private List<String> crosses = new ArrayList<String>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Toss anything with a subject
    if (!subject.isEmpty()) return false;

    // Throw out any version B calls
    if (body.contains("|")) return false;

    // Throw out NCStanleyCountyB calls
    if (STANLEY_CALL_PTN.matcher(body).lookingAt()) return false;

    Matcher match = OPER_PTN.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    boolean ok = body.startsWith("CAD:");
    if (!ok) body = "CAD:" + body;

    String specUnit = null;
    match = SPEC_UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      specUnit = match.group(1);
      body = "CAD:" + body.substring(match.end());
    }

    crosses.clear();
    body = MISSING_SEMI_PTN.matcher(body).replaceAll(";");
    if (! super.parseMsg(body, data)) return false;
    if (data.strAddress.isEmpty() || data.strCall.isEmpty()) return false;
    if (data.strCity.equals("OOC")) data.defCity = data.strCity = "";
    if (!ok && data.strCity.length() <= 3 && data.strCallId.isEmpty()) return false;

    // Cross streets accumulated before an ID/SRC/UNIT field really are cross streets
    // But if we never found an ID/SRC/UNIT, then they are INFO fields
    if (data.strCallId.length() > 0 || data.strSource.length() > 0 || data.strUnit.length() > 0) {
      for (String cross : crosses) {
        if (data.strPlace.length() == 0 && data.strCross.length() == 0 &&
            !cross.endsWith("COUNTY LINE") && !cross.contains("RAMP") &&
             (cross.endsWith(" THE RUN") || !isValidCrossStreet(cross))) {
          data.strPlace = cross;
        } else {
          data.strCross = append(data.strCross, " & ", cross);
        }
      }
    } else {
      for (String cross : crosses) {
        data.strSupp = append(data.strSupp, " / ", cross);
      }
    }
    crosses.clear();

    // Add special unit if found
    if (specUnit != null) data.strUnit = append(specUnit, ",", data.strUnit);
    return true;
  }

  @Override
  public String getProgram() {
    String result = super.getProgram();
    if (result.indexOf("UNIT") < 0) result = "UNIT " + result;
    return result + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new BaseCancelField("DUPLICATE PAGE");
//    if (name.equals("MAP")) return new MapField("[A-Z]\\d{3}", true);
//    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X_PLACE")) return new CrossPlaceField();
    if (name.equals("SRC")) return new SourceField("[A-Z]{2}", true);
    if (name.equals("UNIT")) return new MyUnitField();
//    if (name.equals("UNIT2")) return new UnitField("\\d{3}|CPD");
    if (name.equals("ID")) return new IdField("\\d{8,10}", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // There are two consequetive fields containing the address and call
      // description that can be in either order.  The first one checks
      // to see whether it or the following field looks like a better address
      int status1 = checkAddress(field);
      int status2 = checkAddress(getRelativeField(+1));
      if (status1 <= status2) return false;
      parse(field, data);
      return true;
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strCity.isEmpty()) return false;
      return super.checkParse(field, data);
    }
  }

  private static final Pattern CHANNEL_PTN = Pattern.compile("OPS\\d+");

  private class CrossPlaceField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("(S) (N)")) {
        data.strPlace = append(data.strPlace, " - ", field.substring(7).trim());
      } else if (CHANNEL_PTN.matcher(field).matches()) {
        data.strChannel = field;
      } else  {
        crosses.add(field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CH? PLACE X";
    }

  }

  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      String[] terms = field.split(",");
      StringBuffer units = new StringBuffer();
      StringBuffer chan = new StringBuffer();
      boolean first = true;
      for (String term : terms) {
        if (first && !UNIT_PTN.matcher(term).matches())
          return false;
        if (term.startsWith("OPS")) {
          if (chan.length() > 0) chan.append(',');
          chan.append(term);
        } else {
          if (units.length() > 0) units.append(',');
          units.append(term);
        }
        first = false;
      }
      if (units.isEmpty()) return false;
      data.strUnit = units.toString();
      if (!chan.isEmpty()) data.strChannel = chan.toString();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CH";
    }
  }

  private static final String[] OCC_CITY_LIST = new String[] {
      "FAIRVIEW"
  };
}
