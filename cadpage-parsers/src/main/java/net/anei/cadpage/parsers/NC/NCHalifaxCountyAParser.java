package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCHalifaxCountyAParser extends DispatchA3Parser {
  
  public NCHalifaxCountyAParser() {
    super(0, Pattern.compile("HalifaxCoE911:\\*|\\*|"), "HALIFAX COUNTY", "NC");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupMultiWordStreets("DR M L KING JR");
  }
  
  @Override
  public String getFilter() {
    return "HalifaxCoE911@HalifaxNC911.com,HalifaxCoE911@HalifaxNC.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("I95 ")) {
      data.strAddress = append(data.strAddress, " ", data.strApt);
      data.strAddress = append(data.strAddress, " ", data.strPlace);
      data.strApt = data.strPlace = "";
    }
    return true;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "I95 NB 150",                           "+36.131172,-77.792823",
      "I95 NB 150.5",                         "+36.137819,-77.788965",
      "I95 NB 151",                           "+36.144730,-77.786462",
      "I95 NB 151.5",                         "+36.105333,-77.784720",
      "I95 NB 152",                           "+36.158785,-77.782134",
      "I95 NB 152.5",                         "+36.165255,-77.777737",
      "I95 NB 153",                           "+36.171110,-77.772964",
      "I95 NB 153.5",                         "+36.177344,-77.767992",
      "I95 NB 154",                           "+36.184002,-77.764677",
      "I95 NB 154.5",                         "+36.191172,-77.761958",
      "I95 NB 155",                           "+36.197994,-77.593960",
      "I95 NB 155.5",                         "+36.205000,-77.756799",
      "I95 NB 156",                           "+36.211678,-77.754186",
      "I95 NB 156.5",                         "+36.218654,-77.750813",
      "I95 NB 157",                           "+36.224131,-77.746857",
      "I95 NB 157.5",                         "+36.231085,-77.741514",
      "I95 NB 158",                           "+36.237050,-77.736922",
      "I95 NB 158.5",                         "+36.243333,-77.732125",
      "I95 NB 159",                           "+36.249691,-77.728103",
      "I95 NB 159.5",                         "+36.256876,-77.725909",
      "I95 NB 160",                           "+36.263702,-77.723904",
      "I95 NB 160.5",                         "+36.272010,-77.721759",
      "I95 SB 150",                           "+36.131172,-77.792823",
      "I95 SB 150.5",                         "+36.137819,-77.788965",
      "I95 SB 151",                           "+36.144730,-77.786462",
      "I95 SB 151.5",                         "+36.105333,-77.784720",
      "I95 SB 152",                           "+36.158785,-77.782134",
      "I95 SB 152.5",                         "+36.165255,-77.777737",
      "I95 SB 153",                           "+36.171110,-77.772964",
      "I95 SB 153.5",                         "+36.177344,-77.767992",
      "I95 SB 154",                           "+36.184002,-77.764677",
      "I95 SB 154.5",                         "+36.191172,-77.761958",
      "I95 SB 155",                           "+36.197994,-77.593960",
      "I95 SB 155.5",                         "+36.205000,-77.756799",
      "I95 SB 156",                           "+36.211678,-77.754186",
      "I95 SB 156.5",                         "+36.218654,-77.750813",
      "I95 SB 157",                           "+36.224131,-77.746857",
      "I95 SB 157.5",                         "+36.231085,-77.741514",
      "I95 SB 158",                           "+36.237050,-77.736922",
      "I95 SB 158.5",                         "+36.243333,-77.732125",
      "I95 SB 159",                           "+36.249691,-77.728103",
      "I95 SB 159.5",                         "+36.256876,-77.725909",
      "I95 SB 160",                           "+36.263702,-77.723904",
      "I95 SB 160.5",                         "+36.272010,-77.721759"
     
  });
}
