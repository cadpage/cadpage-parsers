package net.anei.cadpage.parsers.OH;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHGeaugaCountyBParser extends FieldProgramParser {

  public OHGeaugaCountyBParser() {
    super("GEAUGA COUNTY", "OH",
          "SRC! Addr:ADDR! Type:CALL_INFO!");
  }

  @Override
  public String getFilter() {
    return "OH_GC_ENS@CO.GEAUGA.OH.US,CAD@co.geauga.oh.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "CAD:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL_INFO")) return new MyCallInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_DELIM = Pattern.compile("[:;,]");
  private static final Pattern ADDR_APT1_PTN = Pattern.compile(" *\\b(?:Apt|Rm|Room|Lot|Suite|Unit|Bed)\\b[ #]*", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT2_PTN = Pattern.compile("(.*?) {2,}(\\d{1,3}[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT3_PTN = Pattern.compile("\\d{1,3}[A-Z]?|[A-Z]$", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_CITY_OF_PTN = Pattern.compile("\\bCity of +", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_CITY_TRAIL_JUNK_PTN = Pattern.compile(" +(?:Village|Vlg|Vl|#?(\\S*\\d\\S*))$", Pattern.CASE_INSENSITIVE);

  private class MyAddressField extends AddressField {

    private class AptBuilder {
      private List<String> list = new ArrayList<>();

      public void add(String apt) {
        if (apt.isEmpty()) return;
        if (!list.contains(apt)) list.add(apt);
      }

      public void add(AptBuilder apt) {
        list.addAll(apt.list);
      }

      public String toString() {
        if (list.isEmpty()) return "";
        if (list.size() == 1) return list.get(0);
        StringBuilder sb = new StringBuilder();
        for (int jj = list.size()-1; jj >= 0; jj--) {
          if (sb.length() > 0) sb.append('-');
          sb.append(list.get(jj));
        }
        return sb.toString();
      }

    }

    @Override
    public void parse(String field, Data data) {

      String[] terms = ADDR_DELIM.split(field);
      AptBuilder apt = new AptBuilder();
      for (int termNdx = terms.length-1; termNdx > 0; termNdx--) {
        String term = terms[termNdx].trim();
        Matcher match = ADDR_APT1_PTN.matcher(term);
        if (match.find()) {
          String lead = term.substring(0, match.start());
          String trail = term.substring(match.end());
          trail = parseCity(trail, data, apt);
          apt.add(trail);
          lead = parseCity(lead, data, apt);
          parsePlace(lead, data, apt);
        }
        else {
          match = ADDR_APT2_PTN.matcher(term);
          if (match.matches()) {
            term = match.group(1);
            apt.add(match.group(2));
          }
          term = parseCity(term, data, apt);
          if (ADDR_APT3_PTN.matcher(term).matches()) {
            apt.add(term);

          } else {
            parsePlace(term, data, apt);
          }
        }
      }
      super.parse(terms[0], data);
      if (!data.strApt.isEmpty()) apt.add(data.strApt);
      data.strApt = apt.toString();
    }

    /**
     * Parse city from end of field
     * @param field data field
     * @param data parse data results
     * @return remainder of field after city information has been stripped off
     */
    private String parseCity(String field, Data data, AptBuilder apt) {

      // If we already found a city, don't try to parse another one
      if (data.strCity.length() > 0) return field;

      // Look for City of marker, which makes life pretty easy
      Matcher match = ADDR_CITY_OF_PTN.matcher(field);
      if (match.find()) {
        String city = cleanCity(field.substring(match.end()), apt);
        String tmp = checkCity(city);
        if (tmp != null) city = tmp;
        data.strCity = city;
        return field.substring(0, match.start()).trim();
      }

      // We have to do this the hard way
      // start by cleaning of the data field city information
      AptBuilder tapt = new AptBuilder();
      String field2 = cleanCity(field, tapt);

      // Scan through each blank separated word in data field
      int pt = 0;
      while (true) {

        // For each starting word, retrieve remainder of data field
        // If resulting length is less than 5, give up
        String tmp = field2.substring(pt).toUpperCase();
        if (tmp.length() < 4) break;

        // See if remainder is in our city table
        tmp = checkCity(tmp);
        if (tmp != null) {
          data.strCity = tmp;
          apt.add(tapt);
          return field2.substring(0,pt).trim();
        }

        // No go, bump pt to start of next word
        pt = field2.indexOf(' ', pt);
        if (pt < 0) break;
        pt++;
      }

      // No city found, return original field
      return field;
    }

    private String cleanCity(String city, AptBuilder apt) {
      Matcher match = ADDR_CITY_TRAIL_JUNK_PTN.matcher(city);
      if (match.find()) {
        city = city.substring(0,match.start()).trim();
        String tmp = match.group(1);
        if (tmp != null) apt.add(tmp);
      }
      city = city.replace("Township", "Twp");
      city = city.replace("TOWNSHIP", "TWP");
      return city;
    }

    /**
     * Check if city matches anything in our list of city names
     * @param city city name to be checked
     * @return expanded city name if found, null otherwise
     */
    private String checkCity(String city) {

      // Run through our list of city names checking to see if
      // the field remained might be a truncated city name.  If it
      // matches, save this as the city and return the field city prefix
      city = city.toUpperCase();
      if (city.equals("OOGC")) return "Chargrin Falls";
      for (String tCity : CITY_LIST) {
        if (tCity.toUpperCase().startsWith(city)) return tCity;
      }
      return null;
    }

    private void parsePlace(String place, Data data, AptBuilder apt) {
      int pt = place.indexOf('#');
      if (pt >= 0) {
        apt.add(place.substring(pt+1).trim());
        place = place.substring(0,pt).trim();
      }
      data.strPlace = append(place, " - ", data.strPlace);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE APT CITY";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4} - (?:[A-Za-z' ]+(?: (?:BB|CHPD|GCSO|SO)|\\.\\.\\.))? *");
  private static final Pattern CALL_INFO_PTN = Pattern.compile("([-A-Za-z0-9 ]+?) - ");
  private class MyCallInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      Matcher match = CALL_INFO_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strCall = match.group(1).trim();
        data.strSupp = field.substring(match.end()).trim();
      } else {
        int pt = field.indexOf('\n');
        if (pt >= 0) {
          data.strCall = stripFieldEnd(field.substring(0,pt).trim(), "-");
          data.strSupp = field.substring(pt+1).trim();
        } else {
          data.strCall = stripFieldEnd(field, "-");
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL INFO";
    }
  }

  private static final String[] CITY_LIST = new String[]{
    // Cities
    "Aquilla",
    "Burton",
    "Chardon",
    "Hunting Valley",
    "Middlefield",
    "South Russell",

    // Census-designated places
    "Bainbridge",
    "Chesterland",

    // Townships
    "Auburn Twp",
    "Bainbridge Twp",
    "Burton Twp",
    "Chardon Twp",
    "Chester Twp",
    "Claridon Twp",
    "Hambden Twp",
    "Huntsburg Twp",
    "Middlefield Twp",
    "Montville Twp",
    "Munson Twp",
    "Newbury Twp",
    "Parkman Twp",
    "Russell Twp",
    "Thompson Twp",
    "Troy Twp",

    // Other localities
    "East Claridon",
    "Materials Park",
    "Montville",
    "Parkman",
    "Novelty",
    "Welshfield",

    // Ashtabula County
    "Ashtabula County",
    "Harpersfield Twp",
    "Hartsgrove Twp",
    "Trumbull Twp",
    "Windsor",
    "Windsor Twp",

    // Cuyahoga County
    "Cuyahoga County",
    "Bentleyville",
    "oogc",                // Chargrin Falls
    "Chargrin Falls",
    "Chargrin Falls Twp",
    "Mayfield",
    "Mayfield Heights",
    "Gates Mills",
    "Glenwillow",
    "Hunting Valley",
    "Moreland Hills",
    "Orange",
    "Pepper Pike",
    "Solon",
    "Woodmere",


    // Lake County
    "Lake County",
    "Concord Twp",
    "Eastlake",
    "Fairport Harbor",
    "Grand River",
    "Kirtland",
    "Kirtland Hills",
    "Lakeline",
    "Leroy Twp",
    "Madison",
    "Madison Twp",
    "Mentor",
    "Mentor-on-the-Lake",
    "North Madison",
    "North Perry",
    "Painesville (county seat)",
    "Painesville on the Lake",
    "Painesville Twp",
    "Perry",
    "Perry Twp",
    "Timberlake",
    "Unionville",
    "Waite Hill",
    "Wickliffe",
    "Willoughby",
    "Willoughby Hills",
    "Willowick",

    // Portage County
    "Portage County",
    "Aurora",
    "Freedom Twp",
    "Garrettsville",
    "Hiram",
    "Hiram Twp",
    "Mantua",
    "Mantua Twp",
    "Nelson Twp",
    "Shalersville Twp",
    "Streetsboro",
    "Windham",
    "Windham Twp",

    // Trumbull County
    "Trumbull County",
    "Mesopotamia Twp",
    "Farmington Twp",
    "Southington Twp",
    "West Farmington"
  };
}
