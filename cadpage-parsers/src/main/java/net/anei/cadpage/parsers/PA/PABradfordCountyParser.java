package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class PABradfordCountyParser extends DispatchA65Parser {

  public PABradfordCountyParser() {
    super(CITY_LIST, "BRADFORD COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "@bradfordco.org,bradfordpa911,@911email.net,@nlamerica.com,911comm1.info,@911comm3.info,@911comm2.info,911com3.info,@Bradfordco911.info";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(.*[^\n])\n([^\n]+)", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Fix extraneous subject problem
    int pt = subject.lastIndexOf('|');
    if (pt >= 0) subject = subject.substring(pt+1).trim();

    // A double line break gets lost when call is split into two messages, so we
    // need to restore it
    Matcher match = MISSING_BRK_PTN.matcher(body);
    if (match.matches()) body = match.group(1) + "\n\n" + match.group(2);

    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = convertCodes(data.strCity.toUpperCase(), MISSPELLED_CITIES);

    if (data.strState.length() == 0 && NY_CITY_TABLE.contains(data.strCity)) data.strState = "NY";
    return true;
  }

  private static final String[] CITY_LIST = new String[]{

    // Boroughs
    "ALBA",
    "ATHENS",
    "BURLINGTON",
    "CANTON",
    "LE RAYSVILLE",
    "MONROE",
    "MONROETON",
    "NEW ALBANY",
    "ROME",
    "SAYRE",
    "SOUTH WAVERLY",
    "SYLVANIA",
    "TOWANDA",
    "NTOWANDA", // Misspelled TOWANDA
    "TROY",
    "WYALUSING",

    // Townships
    "ALBANY",
    "ARMENIA",
    "ASYLUM",
    "ATHENS",
    "BURLINGTON",
    "CANTON",
    "COLUMBIA",
    "FRANKLIN",
    "GRANVILLE",
    "HERRICK",
    "LEROY",
    "LITCHFIELD",
    "MONROE",
    "NORTH TOWANDA",
    "ORWELL",
    "OVERTON",
    "PIKE",
    "RIDGEBURY",
    "ROME",
    "SHESHEQUIN",
    "SMITHFIELD",
    "SOUTH CREEK",
    "SPRINGFIELD",
    "STANDING STONE",
    "STEVENS",
    "TERRY",
    "TOWANDA",
    "TROY",
    "TUSCARORA",
    "ULSTER",
    "WARREN",
    "WELLS",
    "WEST BURLINGTON",
    "WILMOT",
    "WINDHAM",
    "WYALUSING",
    "WYSOX",

    // Census-designated place
    "GREENS LANDING",

    // Unincorporated communities
    "BERRYTOWN",
    "BROWNTOWN",

    // Chemung County, NY
    "CHEMUNG",
    "WELLSBURG",

    // Tioga County, NY
    "BARTON",
    "LOCKWOOD",
    "WAVERLY"
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "NTOWANDA", "TOWANDA"
  });

  private static final Set<String> NY_CITY_TABLE = new HashSet<String>(Arrays.asList(new String[]{
      "BARTON",
      "CHEMUNG",
      "LOCKWOOD",
      "WELLSBURG",
      "WAVERLY"
  }));
}
