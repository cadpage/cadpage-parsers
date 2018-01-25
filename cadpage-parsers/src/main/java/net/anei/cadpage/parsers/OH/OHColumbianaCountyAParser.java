package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.general.GeneralParser;;


public class OHColumbianaCountyAParser extends GeneralParser {
  
  private static final Pattern CITY_PTN = Pattern.compile(" +([A-Z]+?) +OHIO\\b(?: +[A-Z]+ +COUNTY\\b)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern TWP_PTN = Pattern.compile(" +[A-Z]+ +COUNTY +([ A-Z]+? TWP\\b)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SOURCE_PTN = Pattern.compile("^This email was sent by: (.*)\n+");
  private static final Pattern ADDRESS_QUAL_PTN = 
      Pattern.compile(" +(NEXT TO|(?:IN )?FRONT OF|(?:JUST )?BEFORE) ", Pattern.CASE_INSENSITIVE);

  public OHColumbianaCountyAParser() {
    super("COLUMBIANA COUNTY", "OH");
    setupMultiWordStreets("CALCUTTA SMITH FERRY", "CALCUTTA SMITHFERRY");
    setFieldList("SRC CALL ADDR APT DATE TIME X INFO CITY");
    setupSaintNames("JACOB", "JOSEPH");
  }
  
  @Override
  public String getFilter() {
    return "leetoniafd@hotmail.com,messaging@emergencysmc.com";
  }
  
  // We did enough work on this that users really ought to pay for it, overriding the
  // General location sponsorship
  @Override
  public String getSponsor() {
    return null;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Eliminate OHColumbianaCountyB alerts
    if (body.startsWith("- CALL") || subject.endsWith("- CALL")) return false;
    if (body.startsWith("NATURE:")) return false;
    
    // See if we can find a city
    Matcher match = CITY_PTN.matcher(body);
    if (match.find()) {
      data.strCity = match.group(1);
      body = body.substring(0,match.start()) + body.substring(match.end());
    }
    
    else {
      match = TWP_PTN.matcher(body);
      if (match.find()) {
        data.strCity = match.group(1);
        body = body.substring(0,match.start()) + body.substring(match.end());
      }
    }
    
    match = SOURCE_PTN.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1).trim();
      body = body.substring(match.end()).trim();
      
      // Most calls with a source prefix do have a well defined structure
      // But if there is only one line, drop out and use the general location logic
      String[] flds = body.split("\n+");
      if (flds.length >= 2) {
        for (int ndx = 0; ndx < flds.length; ndx++) {
          flds[ndx] = flds[ndx].trim().replaceAll("  +", " ");
        }
        
        data.strCall = flds[0].trim();
        
        // Determining if line 1 is a place or address is tricky.
        int ndx = 1;
        if (! parseAddressLine(flds[ndx], data, flds.length == 2)) {
            
          ndx = 2;
          if (parseAddressLine(flds[ndx], data, false)) {
            data.strPlace = flds[1];
          } else {
            ndx = 1;
            parseAddressLine(flds[ndx], data, true);
          }
        }
        
        // Everything following the address is an info
        for ( ndx++ ; ndx < flds.length; ndx++) {
          String fld = flds[ndx];
          if (isValidAddress(fld)) {
            data.strCross = append(data.strCross, " & ", fld);
          } else {
            data.strSupp = append(data.strSupp, " / ", fld);
          }
        }
        return true;
      }
    }
    
    // Apply general location logic
    return super.parseMsg(subject, body, data);
  }
  
  
  /**
   * Parse prospective address line
   * @param line line to be parsed
   * @param data parse information data object
   * @param force true if this line has been positively identified as an address
   * @return true if line was parsed as an address, false otherwise
   */
  private boolean parseAddressLine(String line, Data data, boolean force) {
    line = line.trim();
    String info = "";
    Matcher match = ADDRESS_QUAL_PTN.matcher(line);
    if (match.find()) {
      info = line.substring(match.start(1));
      line = line.substring(0,match.start()).trim();
    }
    if (force || isValidAddress(line)) {
      parseAddress(line, data);
      data.strSupp = info;
      return true;
    } else {
      return false;
    }
  }
    
    
  
  @Override
  protected int getParseAddressFlags() {
    return FLAG_NO_IMPLIED_APT | FLAG_AT_SIGN_ONLY;
  }
}
