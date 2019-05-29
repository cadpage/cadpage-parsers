package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Lebanon County, PA
 */
public class PALebanonCountyParser extends SmartAddressParser {

  public PALebanonCountyParser() {
    super("LEBANON COUNTY", "PA");
    setFieldList("SRC TIME DATE ADDR APT CITY X PLACE PRI CALL CH UNIT BOX INFO");
    removeWords("ALY", "PLAZA", "TERRACE");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY;
  }
  
  @Override
  public String getFilter() {
    return "km911alert@gmail.com,km911@fastmail.fm,7176798487";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("([ A-Za-z0-9]+)@(\\d\\d:\\d\\d)");
  private static final Pattern CALL_PREFIX_PTN = Pattern.compile(" (?:Med Class(\\d) |((?:<Call Type>|\\(Call Type\\)|Aircraft|CARDIAC|CHEST PAIN|DIFFICULTY|FIRE|GENERAL|MED|MVA|Non-Emergency|[Ss]ick [Pp]erson|Stand-by|TRAF|Traffic|TRANSFER|Land Search&Rescue|Unresponsive)[- ])|(?<=[ a-z]|^|PLAZA|AUTOMOTIVE)((?!APT)[A-Z]{2,6} ?-(?!Box) ?))");
  private static final Pattern COUNTY_CITY_PTN = Pattern.compile("(?:([BDLS]C) )?(?:City of ([A-Z]+)|((?:(?:NORTH|SOUTH|EAST|WEST|UPPER|LOWER) )?(?:(?:LITTLE|MOUNT|MT|NEW|PORT|ST) )?(?:[A-Z]+|COLD SPRING|DEER LAKE|PALO ALTO|SCHUYLKILL HAVEN|PINE GROVE|SINKING SPRING|TERRE HILL) (?:BORO(?:UGH)?|TWP|TOWNSHIP|CITY)))\\b *", Pattern.CASE_INSENSITIVE);
  private static final Pattern BOX_PTN =  Pattern.compile(" Fire-Box (?:([-0-9]+) )?EMS-Box(?: ([-0-9]+))?");
  private static final Pattern CLASS_PTN = Pattern.compile("(?:[* ]+|^)(?:EMS|Med) [Cc]lass ?(\\d)[* ]+");
  private static final Pattern DELIMS = Pattern.compile("[, ]+");
  private static final Pattern CH_PTN = Pattern.compile("(FG)[- 0]*(\\d+)\\b *");
  private static final Pattern UNIT_PTN = Pattern.compile("(?:[, ]+|^)([A-Z]+[0-9]+(?:-\\d+|ST\\d){0,2}|[0-9]+[A-Z]+|\\d+-\\d*|DUTY?|L|NOPD|PSP-N|REQ|SQ|FG[ -]?\\d+)$", Pattern.CASE_INSENSITIVE);

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1).toUpperCase().replace(" ", "");
      data.strTime = match.group(2);
    }
    
    // Look for a priority/call prefix pattern that marks the
    // end of the address
    match = CALL_PREFIX_PTN.matcher(body);
    if (!match.find()) {
      if (data.strSource.length() == 0) return false;
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
    
    String sAddress = body.substring(0,match.start()).trim();
    data.strPriority = getOptGroup(match.group(1));
    String sCallPfx = match.group(2);
    if (sCallPfx == null) sCallPfx = match.group(3);
    String sTail = body.substring(match.end()).trim();
    
    // Looks like we can usually count on a comma separator following the address
    pt = sAddress.indexOf(',');
    if (pt < 0) {
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, sAddress, data);
    } else { 
      String sAddr = sAddress.substring(0,pt).trim().replace('@', '&');
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, sAddr, data); 
      sAddress = sAddress.substring(pt+1).trim();

      // We usually have pretty good luck using a pattern to identify the country and city
      match = COUNTY_CITY_PTN.matcher(sAddress);
      if (!match.lookingAt()) {
        
        // If that did not work, see if it starts at the next comma
        // and make this segment an apartment
        pt = sAddress.indexOf(',');
        if (pt < 0) return false;
        data.strApt = append(data.strApt, "-", sAddress.substring(0,pt).trim());
        sAddress = sAddress.substring(pt+1).trim();

        match = COUNTY_CITY_PTN.matcher(sAddress);
        if (!match.lookingAt()) return false;
      }
      
      String county = match.group(1);
      String city = match.group(2);
      if (city == null) city = match.group(3);
      if (county != null) city = city + ", " + COUNTY_CODES.getProperty(county);
      data.strCity = city;
      
      sAddress = sAddress.substring(match.end());
      while (sAddress.length() > 0) {
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, sAddress);
        if (!res.isValid()) break;
        String save = data.strCross;
        res.getData(data);
        data.strCross = append(save, ", ", data.strCross);
        sAddress = res.getLeft();
      }
      data.strPlace = sAddress;
    }

    // That is it for the address section, now work on the tail including the call description
    // Start by stripping off the combined box number

    match = BOX_PTN.matcher(sTail);
    if (! match.find()) {
      pt = sTail.indexOf(" Fire-");
      if (pt >= 0) sTail = sTail.substring(0,pt).trim();
    } else {
      String fireBox = match.group(1);
      String emsBox =  match.group(2);
      fireBox = (fireBox != null ? "Fire:"+fireBox : "");
      emsBox = (emsBox != null ? "EMS:" + emsBox : "");
      data.strBox = append(fireBox, " ", emsBox);
      sTail = sTail.substring(0,match.start()).trim();
    }

    // If there is a class priority it separates the call description from the units
    String unit = "";
    if (sTail.length() > 0) {
      match = CLASS_PTN.matcher(sTail);
      if (match.find()) {
        data.strPriority = match.group(1);
        unit = sTail.substring(match.end()).trim().toUpperCase();
        sTail = sTail.substring(0, match.start()).trim();
        unit = DELIMS.matcher(unit).replaceAll(" ").trim();
      }
    
      // If there is not class priority, we need to strip off individual units
      
      else {
        while (true) {
          match = UNIT_PTN.matcher(sTail);
          if (!match.find()) break;
          unit = append(match.group(1).toUpperCase().replace(' ', '-'), " ", unit);
          sTail = sTail.substring(0,match.start()).trim();
        }
      }

      // Strip leading channel from unit info
      while ((match = CH_PTN.matcher(unit)).lookingAt()) {
        data.strChannel = match.group(1) + '-' + match.group(2);
        unit = unit.substring(match.end());
      }
      data.strUnit = unit;
    }
    
    data.strCall = ((sCallPfx == null ? "" : sCallPfx) + sTail).trim();
    if (data.strCall.length() == 0) data.strCall = "Med";
    
    // Make some validity checks to require **SOMETHING** beyond a simple call prefix match
    return data.strCity.length() > 0 || data.strBox.length() > 0 || data.strUnit.length() > 0;
  }
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[]{
      "BC", "BERKS COUNTY",
      "DC", "DAUPHIN COUNTY",
      "LC", "LANCASTER COUNTY",
      "SC", "SCHUYLKILL COUNTY"
  });
}
