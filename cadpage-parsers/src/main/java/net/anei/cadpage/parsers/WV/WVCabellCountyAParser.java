package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA67Parser;


public class WVCabellCountyAParser extends DispatchA67Parser {

  private static final Pattern MARKER_PTN = Pattern.compile("^CCERC911\n:");

  public WVCabellCountyAParser() {
    super(CITY_LIST, "CABELL COUNTY", "WV", A67_OPT_PLACE | A67_OPT_CROSS, null, "(?:\\b(?!APT|LOT|I\\d|UNIT)[A-Z]{1,4}\\d{1,3}\\b *)+");
    setupMultiWordStreets("R FORK MERRITTS CREEK", "R FORK RACCOON CREEK");
    setupSpecialStreets("MILL CREEK CROSSING", "RUSSELL CREEK", "TEAYS MEADOWS");
  }

  @Override
  public String getFilter() {
    return "dispatch@ccerc911.org,777";
  }

  private static final Pattern PUTNAM_CO_PTN = Pattern.compile("C PUTNAM|PUTNAM\\b.*");
  private static final Pattern ST_NNN_PTN = Pattern.compile(".*(?: |^)ST\\d+(?: |$)");
  private static final Pattern CITY_DELIM_PTN = Pattern.compile(" IN |,");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER_PTN.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    body = body.replace("\n", "");
    if (!super.parseMsg(body,  data)) return false;

    // Mutual aid calls to Putnam county are different

    if (PUTNAM_CO_PTN.matcher(data.strAddress).matches()) {
      if (data.strPlace.length() > 0) {
        String tmp = parseMutualAddress(true, data.strPlace, data);
        if (tmp != null) data.strPlace = tmp;
      }

      else {
        Parser p = new Parser(data.strSupp);
        String addr1 = p.get(" / ");
        String tmp = parseMutualAddress(true, addr1, data);
        if (tmp != null) {
          data.strSupp = append(tmp, " / ", p.get());
        } else {
          String addr2 = p.get(" / ");
          tmp = parseMutualAddress(false, addr2, data);
          if (tmp != null) {
            data.strCall = append(data.strCall, " - ", addr1);
            data.strSupp = append(tmp, " / ", p.get());
          }
        }
      }
    }

    else {
      int pt = data.strCall.lastIndexOf('/');
      if (pt >= 0) {
        String call = data.strCall.substring(0,pt).trim();
        String addr = data.strCall.substring(pt+1).trim();
        if (PUTNAM_CO_PTN.matcher(addr).matches()) {
          data.strCall = call;
          if (data.strCity.length() == 0) data.strCity = "PUTNAM COUNTY";
        }

        else {
          if (call.equals("Mutual Aid")) {
            String saveCall = data.strCall;
            String saveAddr = data.strAddress;
            data.strCall = call;
            String tmp = parseMutualAddress(true, addr, data);
            if (tmp != null) {
              data.strCross = append(saveAddr, " & ", data.strCross);
            } else {
              data.strCall = saveCall;
            }
          }
        }
      }
    }
    return true;
  }

  private String parseMutualAddress(boolean leadCall, String addr, Data data) {

    // They occasionally through an STnnn unit name into the leading call description
    // which confuses the address logic because it looks like a state route number.
    // So we have to take some steps to remove it
    StartType st = StartType.START_ADDR;
    String callPrefix = null;
    if (leadCall) {
      st = StartType.START_OTHER;
      Matcher match = ST_NNN_PTN.matcher(addr);
      if (match.lookingAt()) {
        callPrefix = match.group().trim();
        if (CITY_DELIM_PTN.matcher(callPrefix).find()) {
          callPrefix = null;
        } else {
          addr = addr.substring(match.end()).trim();
        }
      }
    }

    Result res;
    String left;
    Matcher match = CITY_DELIM_PTN.matcher(addr);
    if (match.find()) {
      left = addr.substring(match.end()).trim();
      addr = addr.substring(0,match.start()).trim();
      res = parseAddress(st, FLAG_NO_CITY | FLAG_ANCHOR_END, addr);
      if (!res.isValid()) return null;
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, left, data);
      left = getLeft();
    } else {
      res = parseAddress(st, addr);
      if (!res.isValid()) return null;
      left = res.getLeft();
    }

    data.strAddress = "";
    res.getData(data);
    if (data.strCity.length() == 0) data.strCity = "PUTNAM COUNTY";

    if (leadCall) {
      String call = res.getStart();
      if (callPrefix != null) call = append(callPrefix, " ", call);
      data.strCall = append(data.strCall, " - ", call);
    }
    return left;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("ADDR", "ADDR CITY? X?");
  }

  private static final String[] CITY_LIST = new String[]{

    // City
    "HUNTINGTON",

    // Town
    "MILTON",

    // Village
    "BARBOURSVILLE",

    // Census-designated places
    "CULLODEN",
    "LESAGE",
    "PEA RIDGE",
    "SALT ROCK",

    // Unincorporated communities
    "ALTIZER",
    "BROWNSTOWN",
    "CLOVER",
    "FUDGES CREEK",
    "HODGES",
    "INDIAN MEADOWS",
    "INEZ",
    "JOHNSON",
    "MELISSA",
    "ONA",
    "PRAIRIETOWN",
    "REID",
    "ROACH",
    "SARAH",
    "SWANN",
    "WILSON",

    // Mason County
    "GLENWOOD",

    // Putnam County
    "PUTNAM COUNTY",
    "PUTNAM CO",
    "HURRICAN",
    "HURRICANE",
    "SCOTT DEPOT",
    "TEAYS",
    "TEAYS VALLEY"
  };
}
