import {StyleSheet} from 'react-native';

/**
 * Organized stylesheets for HomeScreen
 */
export const styles = StyleSheet.create({
  // defines entire container
  container: {
    flex: 1,
    backgroundColor: '#fffdd1',
    alignItems: 'center',
    justifyContent: 'center',
  },
  // title and preferences button
  topMenu: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    backgroundColor: '#7c0a02',
    alignSelf: 'stretch',
    paddingRight: 10,
  },
  // profile card area
  matchMenu: {
    flex: 6,
    justifyContent: 'center',
    alignItems: 'center',
  },
  // match/deny buttons
  bottomMenu: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  // preferences popup menu
  preferencesMenu: {
    backgroundColor: '#fffdd1',
    height: '100%',
    padding: 10,
  },
  // text within preferences menu
  preferencesText: {
    fontSize: 24,
    fontFamily: 'sans-serif-medium',
    paddingTop: 10,
    color: 'black',
  },
  // age text input styling
  preferencesInput: {
    height: 40,
    backgroundColor: 'white',
    width: '12.5%',
    margin: 10,
    borderWidth: 1,
    padding: 10,
  },
  // BookBae title
  title: {
    color: 'white',
    fontSize: 48,
    fontFamily: 'sans-serif-medium',
    paddingLeft: 20,
    padding: 10,
  },
  // all normal (non-icon) buttons
  button: {
    alignItems: 'center',
    alignSelf: 'center',
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: '#BD2A2A',
    marginTop: 5,
    marginBottom: 5,
  },
  // text for normal buttons.
  buttonText: {
    fontSize: 18,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white',
  },
});

export const matchStyles = StyleSheet.create({
  // size of profile card container
  matchBox: {
    height: 475,
    width: 300,
    backgroundColor: 'white',
    borderRadius: 25,
    justifyContent: 'flex-start',
    alignItems: 'center',
    elevation: 15,
    shadowColor: '#7c0a02',
  },
  // container for name and age
  header: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    alignSelf: 'stretch',
    padding: 10,
  },
  // user name
  name: {
    color: 'black',
    fontSize: 32,
    fontWeight: 'bold',
    fontFamily: 'sans-serif-medium',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
  },
  // user age/gender
  age: {
    color: '#242526',
    fontSize: 24,
  },
  // container for user bio
  bioContainer: {
    flex: 3,
    alignSelf: 'stretch',
  },
  // user bio
  bioText: {
    fontSize: 24,
    color: 'black',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    alignSelf: 'stretch',
    padding: 10,
  },
  // user books at bottom of profile card
  bookDisplay: {
    flex: 3,
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    alignItems: 'flex-end',
    padding: 5,
  },
  // books in book display
  book: {
    height: 175,
    width: 125,
    margin: 10,
    borderRadius: 10,
  },
  // icon buttons
  button: {
    margin: 20,
    paddingBottom: 10,
  },
  // button image size
  buttonIcon: {
    height: 115,
    width: 115,
  },
});
