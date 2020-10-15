package net.anei.cadpage.parsers.WA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class WASnohomishCountyFParser extends DispatchA57Parser {
  
  public WASnohomishCountyFParser() {
    super("SNOHOMISH COUNTY", "WA");
  }
  
  @Override
  public String getFilter() {
    return "NoReply@snopac911.us";
  }

  private static final Pattern BAD_APT_PTN = Pattern.compile("AT |EXIT|OFFRAMP|[NSEW]O |MP", Pattern.CASE_INSENSITIVE);
  @Override
  protected boolean isNotExtraApt(String apt) {
    return BAD_APT_PTN.matcher(apt).lookingAt();
  }
}
