package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA39Parser;

/**
 * Cuyahoga County, OH
 */
public class OHCuyahogaCountyAParser extends DispatchA39Parser {

  public OHCuyahogaCountyAParser() {
    super(OHCuyahogaCountyParser.CITY_CODES, "CUYAHOGA COUNTY", "OH");
    setupCities(OHCuyahogaCountyParser.CITY_LIST);
    setupSaintNames("PETERSBURG", "ANDREWS");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    removeWords("RUN", "HTS", "PATH");   // Lest SQUAD RUN be considered an address :(
  }

  @Override
  public String getFilter() {
    return "@tacpaging.com,cvd2-dispatch@cvdispatch.net,dispatch@secc-911.org,dispatch@hhdispatch.net,dispatch@prdc.net,dispatch@waltonhillsohio.gov,dispatch@beachwoodohio.com";
  }

  private static final Pattern MARKER = Pattern.compile("Dispatch Message[:\n]");
  private static final Pattern DIR_BOUND_PTN1 = Pattern.compile("(?:\\b|(?<=\\d))([NSEW])/B\\b");
  private static final Pattern DIR_BOUND_PTN2 = Pattern.compile("/ (NORTHBOUND|EASTBOUND|SOUTHBOUND|WESTBOUND)\\b");
  private static final Pattern OOC_CITY_PTN = Pattern.compile("(.*?)(?: +VLG)?(?: +(OH))?(?: +\\d{5})?");
  private static final Pattern EW_CITY_PTN = Pattern.compile(".*\\d.*");

  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    if (subject.length() == 0) {
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        subject = "Dispatch Message";
        body = body.substring(match.end()).trim();
      }
    }
    body = stripFieldStart(body, "**TEST ACTIVE911**\n");
    body = DIR_BOUND_PTN1.matcher(body).replaceAll("$1B");
    body = DIR_BOUND_PTN2.matcher(body).replaceAll("$1");
    if (!super.parseUntrimmedMsg(subject, body, data)) return false;

    // Mutual aid calls always have a city field in the info section
    data.strAddress = data.strAddress.replace(',', ' ').trim();
    if (data.strCity.isEmpty() && !data.strSupp.isEmpty()) {
      int pt = data.strSupp.indexOf('\n');
      String city = ( pt >= 0 ? data.strSupp.substring(0, pt).trim() : data.strSupp);
      city = city.replace(',', ' ').trim();
      Matcher match = OOC_CITY_PTN.matcher(city);
      if (!match.matches()) return false;               // cannot happen
      city = match.group(1);
      String state = match.group(2);
      if (state != null || isCity(city)) {
        data.strCity = city;
        if (state != null) data.strState = state;
        data.strSupp = data.strSupp.substring(pt+1);    // works even if pt is -1
      }
    }

    // More weird constructs
    Matcher match = EW_CITY_PTN.matcher(data.strCity);
    if (match.matches()) {
      String city = data.strCity;
      data.strCity = "";
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, city, data);
      data.strApt = append(data.strApt, "-", getStart());
    }
    return true;
  }

  private static final Pattern GPS_JUNK_PTN = Pattern.compile("&|\\b[NSEW]B\\b|\\bMM\\b|\\((?:NORTH|SOUTH|EAST|WEST)\\)|\\b(?:NORTH|SOUTH|EAST|WEST) ?BOUND\\b");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = GPS_JUNK_PTN.matcher(address).replaceAll("").trim();
    address = MBLANK_PTN.matcher(address).replaceAll(" ");
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "161 TURNPIKE",               "+41.335479,-81.821512",
      "161.1 TURNPIKE",             "+41.335479,-81.821512",
      "161.2 TURNPIKE",             "+41.335479,-81.821512",
      "161.3 TURNPIKE",             "+41.335479,-81.821512",
      "161.4 TURNPIKE",             "+41.335479,-81.821512",
      "161.5 TURNPIKE",             "+41.335479,-81.821512",
      "161.6 TURNPIKE",             "+41.335479,-81.821512",
      "161.7 TURNPIKE",             "+41.335479,-81.821512",
      "161.8 TURNPIKE",             "+41.335479,-81.821512",
      "161.9 TURNPIKE",             "+41.335479,-81.821512",
      "162 TURNPIKE",               "+41.332391,-81.815604",
      "162.1 TURNPIKE",             "+41.332391,-81.815604",
      "162.2 TURNPIKE",             "+41.332391,-81.815604",
      "162.4 TURNPIKE",             "+41.332391,-81.815604",
      "162.5 TURNPIKE",             "+41.332391,-81.815604",
      "162.6 TURNPIKE",             "+41.332391,-81.815604",
      "162.7 TURNPIKE",             "+41.332391,-81.815604",
      "162.8 TURNPIKE",             "+41.332391,-81.815604",
      "162.9 TURNPIKE",             "+41.332391,-81.815604",
      "163 TURNPIKE",               "+41.328293,-81.798808",
      "163.1 TURNPIKE",             "+41.328293,-81.798808",
      "163.2 TURNPIKE",             "+41.328293,-81.798808",
      "163.3 TURNPIKE",             "+41.328293,-81.798808",
      "163.4 TURNPIKE",             "+41.328293,-81.798808",
      "163.5 TURNPIKE",             "+41.328293,-81.798808",
      "163.6 TURNPIKE",             "+41.328293,-81.798808",
      "163.7 TURNPIKE",             "+41.328293,-81.798808",
      "163.8 TURNPIKE",             "+41.328293,-81.798808",
      "163.9 TURNPIKE",             "+41.328293,-81.798808",
      "164 TURNPIKE",               "+41.321724,-81.781898",
      "164.1 TURNPIKE",             "+41.321724,-81.781898",
      "164.2 TURNPIKE",             "+41.321724,-81.781898",
      "164.3 TURNPIKE",             "+41.321724,-81.781898",
      "164.4 TURNPIKE",             "+41.321724,-81.781898",
      "164.5 TURNPIKE",             "+41.321724,-81.781898",
      "164.6 TURNPIKE",             "+41.321724,-81.781898",
      "164.7 TURNPIKE",             "+41.321724,-81.781898",
      "164.8 TURNPIKE",             "+41.321724,-81.781898",
      "164.9 TURNPIKE",             "+41.321724,-81.781898",
      "165 TURNPIKE",               "+41.315835,-81.764469",
      "165.1 TURNPIKE",             "+41.315835,-81.764469",
      "165.2 TURNPIKE",             "+41.315835,-81.764469",
      "165.3 TURNPIKE",             "+41.315835,-81.764469",
      "165.4 TURNPIKE",             "+41.315835,-81.764469",
      "165.5 TURNPIKE",             "+41.315835,-81.764469",
      "165.6 TURNPIKE",             "+41.315835,-81.764469",
      "165.7 TURNPIKE",             "+41.315835,-81.764469",
      "165.8 TURNPIKE",             "+41.315835,-81.764469",
      "165.9 TURNPIKE",             "+41.315835,-81.764469",
      "166 TURNPIKE",               "+41.307919,-81.748476",
      "166.1 TURNPIKE",             "+41.307919,-81.748476",
      "166.2 TURNPIKE",             "+41.307919,-81.748476",
      "166.3 TURNPIKE",             "+41.307919,-81.748476",
      "166.4 TURNPIKE",             "+41.307919,-81.748476",
      "166.5 TURNPIKE",             "+41.307919,-81.748476",
      "166.6 TURNPIKE",             "+41.307919,-81.748476",
      "166.7 TURNPIKE",             "+41.307919,-81.748476",
      "166.8 TURNPIKE",             "+41.307919,-81.748476",
      "166.9 TURNPIKE",             "+41.307919,-81.748476",
      "167 TURNPIKE",               "+41.303220,-81.731031",
      "167.1 TURNPIKE",             "+41.303220,-81.731031",
      "167.2 TURNPIKE",             "+41.303220,-81.731031",
      "167.3 TURNPIKE",             "+41.303220,-81.731031",
      "167.4 TURNPIKE",             "+41.303220,-81.731031",
      "167.5 TURNPIKE",             "+41.303220,-81.731031",
      "167.6 TURNPIKE",             "+41.303220,-81.731031",
      "167.7 TURNPIKE",             "+41.303220,-81.731031",
      "167.8 TURNPIKE",             "+41.303220,-81.731031",
      "167.9 TURNPIKE",             "+41.303220,-81.731031",
      "168 TURNPIKE",               "+41.302997,-81.711776",
      "168.1 TURNPIKE",             "+41.302997,-81.711776",
      "168.2 TURNPIKE",             "+41.302997,-81.711776",
      "168.3 TURNPIKE",             "+41.302997,-81.711776",
      "168.4 TURNPIKE",             "+41.302997,-81.711776",
      "168.5 TURNPIKE",             "+41.302997,-81.711776",
      "168.6 TURNPIKE",             "+41.302997,-81.711776",
      "168.7 TURNPIKE",             "+41.302997,-81.711776",
      "168.8 TURNPIKE",             "+41.302997,-81.711776",
      "168.9 TURNPIKE",             "+41.302997,-81.711776",
      "169 TURNPIKE",               "+41.298172,-81.693980",
      "169.1 TURNPIKE",             "+41.298172,-81.693980",
      "169.2 TURNPIKE",             "+41.298172,-81.693980",
      "169.3 TURNPIKE",             "+41.298172,-81.693980",
      "169.4 TURNPIKE",             "+41.298172,-81.693980",
      "169.5 TURNPIKE",             "+41.298172,-81.693980",
      "169.6 TURNPIKE",             "+41.298172,-81.693980",
      "169.7 TURNPIKE",             "+41.298172,-81.693980",
      "169.8 TURNPIKE",             "+41.298172,-81.693980",
      "169.9 TURNPIKE",             "+41.298172,-81.693980",
      "170 TURNPIKE",               "+41.292574,-81.675807",
      "170.1 TURNPIKE",             "+41.292574,-81.675807",
      "170.2 TURNPIKE",             "+41.292574,-81.675807",
      "170.3 TURNPIKE",             "+41.292574,-81.675807",
      "170.4 TURNPIKE",             "+41.292574,-81.675807",
      "170.5 TURNPIKE",             "+41.292574,-81.675807",
      "170.6 TURNPIKE",             "+41.292574,-81.675807",
      "170.7 TURNPIKE",             "+41.292574,-81.675807",
      "170.8 TURNPIKE",             "+41.292574,-81.675807",
      "170.9 TURNPIKE",             "+41.292574,-81.675807",
      "171 TURNPIKE",               "+41.284310,-81.657354",
      "171.1 TURNPIKE",             "+41.284310,-81.657354",
      "171.2 TURNPIKE",             "+41.284310,-81.657354",
      "171.3 TURNPIKE",             "+41.284310,-81.657354",
      "171.4 TURNPIKE",             "+41.284310,-81.657354",
      "171.5 TURNPIKE",             "+41.284310,-81.657354",
      "171.6 TURNPIKE",             "+41.284310,-81.657354",
      "171.7 TURNPIKE",             "+41.284310,-81.657354",
      "171.8 TURNPIKE",             "+41.284310,-81.657354",
      "171.9 TURNPIKE",             "+41.284310,-81.657354",
      "172 TURNPIKE",               "+41.278118,-81.643592",
      "172.1 TURNPIKE",             "+41.278118,-81.643592",
      "172.2 TURNPIKE",             "+41.278118,-81.643592",
      "172.3 TURNPIKE",             "+41.278118,-81.643592",
      "172.4 TURNPIKE",             "+41.278118,-81.643592",
      "172.5 TURNPIKE",             "+41.278118,-81.643592",
      "172.6 TURNPIKE",             "+41.278118,-81.643592",
      "172.7 TURNPIKE",             "+41.278118,-81.643592",
      "172.8 TURNPIKE",             "+41.278118,-81.643592",
      "172.9 TURNPIKE",             "+41.278118,-81.643592",
      "173 TURNPIKE",               "+41.271565,-81.624401",
      "173.1 TURNPIKE",             "+41.271565,-81.624401",
      "173.2 TURNPIKE",             "+41.271565,-81.624401",
      "173.3 TURNPIKE",             "+41.271565,-81.624401",
      "173.4 TURNPIKE",             "+41.271565,-81.624401",
      "173.5 TURNPIKE",             "+41.271565,-81.624401",
      "173.6 TURNPIKE",             "+41.271565,-81.624401",
      "173.7 TURNPIKE",             "+41.271565,-81.624401",
      "173.8 TURNPIKE",             "+41.271565,-81.624401",
      "173.9 TURNPIKE",             "+41.271565,-81.624401"
  });
}
