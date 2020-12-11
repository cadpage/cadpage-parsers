package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class COTellerCountyParser extends MsgParser {

  public COTellerCountyParser() {
    super("TELLER COUNTY", "CO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "ept@elpasoteller911.org,alerts@eptpaging.info";
  }

  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\d+\\)");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = body.replace("EIDS SYMPTOMTS PRESENT:", "EIDS SYMPTOMS PRESENT:");
    FParser p = new FParser(body);

    if (p.check("REF:")) {
      if (p.checkAhead(26, "EIDS SYMPTOMS PRESENT:  ")) {
        setFieldList("ADDR APT CALL INFO");
        parseAddress(p.get(26), data);
        data.strCall = p.get(24);
        data.strSupp = p.get();
        return true;
      }
      if (p.checkAhead(33, "THE LOC HAS CHANGED TO:")) {
        setFieldList("CALL ADDR APT PLACE");
        data.strCall = p.get(33);
        p.skip(23);
        parseAddress(p.get(30), data);
        if (!p.check("#")) return false;
        data.strApt = append(data.strApt, "-", p.get(8));
        data.strPlace = p.get();
        return true;
      }
      String addr = p.get(27);
      if (p.check(" ")) return false;
      String info = p.get();
      if (!INFO_PFX_PTN.matcher(info).lookingAt()) return false;
      setFieldList("ADDR APT INFO");
      parseAddress(addr, data);
      data.strSupp = info;
      return true;
    }

    if (p.check("Add: ")) {
      setFieldList("ADDR APT PLACE CALL CITY");
      parseAddress(p.get(35), data);
      data.strPlace = p.get(35);
      if (!p.check("Problem: ")) return false;
      data.strCall = p.get(35);
      if (!p.check("City: ")) return false;
      data.strCity = p.get();
      return true;
    }

    if (p.check("Add:") || p.check("ADD:")) {
//      if (p.checkAhead(80, "Prob:") || p.checkAhead(80, "PROB:")) {
//        setFieldList("ADDR APT CALL");
//        parseAddress(p.get(80), data);
//        p.skip(5);
//        data.strCall = p.get();
//        return true;
//      } else {
        setFieldList("ADDR CALL APT PLACE CODE PHONE");
        parseAddress(p.get(35), data);
        if (!p.check("Problem:")) return false;
        String call = p.getOptional(30, "Apt:");
        if (call != null) {
          data.strCall = call;
          data.strApt = p.get(5);
        } else {
          data.strCall = p.get(35);
        }
        if (!p.check("Loc:")) return false;
        data.strPlace = p.get(70);
        if (!p.check("Code:")) return false;
        data.strCode = p.get(10);
        if (!p.check("RP Phone:")) return false;
        data.strPhone = p.get();
        return true;
      }
//    }

    if (p.check("Add")) {
      setFieldList("ADDR  APT PLACE CALL CITY");
      String addr = p.getOptional(30, "Problem");
      if (addr != null) {
        parseAddress(addr, data);
        data.strCall = p.get();
        return true;
      }
      parseAddress(p.get(35), data);
      data.strPlace = p.get(35);
      if (!p.check("Problem")) return false;
      data.strCall = p.get(35);
      if (!p.check("City")) return false;
      data.strCity = p.get(35);
      return true;
    }

    if (p.check("FIRE/EMS:")) {
      setFieldList("SRC UNIT CALL ADDR APT");
      data.strSource = p.get(32);
      if (!p.check("~")) return false;
      data.strUnit = p.get(30);
      if (!p.check("~")) return false;
      data.strCall = p.get(30);
      if (!p.check("~")) return false;
      parseAddress(p.get(), data);
      return true;

    }

    if (p.checkAheadBlanks(36, 4) && !p.checkAheadBlanks(40, 1)) {
      setFieldList("ADDR APT CALL CODE PLACE PHONE INFO");
      parseAddress(p.get(40), data);
      data.strCall = p.get(30);
      String place = p.getOptional(36, "RP Phone:");
      if (place != null) {
        data.strPlace = place;
      } else {
        data.strCode = p.get(7);
        p.setOptional();
        if (!p.check("RP Ph:") && !p.check("RP PH:")) return false;
      }
      String info = p.get();
      Matcher match = INFO_PHONE_PTN.matcher(info);
      if (match.lookingAt()) {
        data.strPhone = match.group();
        info = info.substring(match.end()).trim();
      }
      return true;
    }

    return false;
  }



  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "28541 N HWY 67",    "39.052499,-105.094141",
      "1364 CR 75",        "39.045712,-105.094569"
  });
}
