package net.anei.cadpage.parsers;

import java.util.HashSet;
import java.util.Set;

/**
 * Common base class for AliasedMsgParser and GroupBestParser
 * adds the ability to report a sender filter that has been
 * merged with sender filters from other parsers
 */
public abstract class GroupBaseParser extends MsgParser {
  
  private String defCity = null;
  private String defState = null;
  private String filter = "";
  private Set<String> filterSet = new HashSet<String>();
  
  /**
   * Main constructor
   * @param parser initial parser
   */
  protected GroupBaseParser() {
    super(null, null);
  }

  protected void addParser(MsgParser parser) {

    // Group block parser isn't a real parser and should not update any of this
    if (parser instanceof GroupBlockParser) return;
    setDefaults(parser.getDefaultCity(), parser.getDefaultState());
    updateFilter(parser.getFilter());
  }
  
  protected void setDefaults(String newCity, String newState) {
    
    if (defCity == null) defCity = newCity;
    else if (!defCity.equals(newCity)) defCity = "";
    
    if (defState == null) defState = newState;
    else if (!defState.equals(newState)) defState = "";
  }
  
  /**
   * Merge new parser filter into combined filter
   * @param filterP
   */
  private void updateFilter(String filterP) {
    for (String term : filterP.split(",")) {
      term = term.trim();
      if (term.length() == 0) continue;
      if (filterSet.add(term.toUpperCase())) {
        filter = append(filter, ",", term);
      }
    }
  }
  
  @Override
  public String getDefaultCity() {
    return defCity;
  }
  
  @Override
  public String getDefaultState() {
    return defState;
  }

  @Override
  public String getFilter() {
    return filter;
  }
}
