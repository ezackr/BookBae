import React from 'react';
import {
  Alert,
  Image,
  Pressable,
  SafeAreaView,
  StyleSheet,
  Text,
  Modal,
  TextInput,
} from 'react-native';
import RadioForm from 'react-native-simple-radio-button';
import Client from '../Client.js';

/**
 * Main App screen used to suggest matches to users.
 */
const HomeScreen = ({navigation}) => {
  // creates options for preferences menu.
  // gender options:
  const [modalVisible, setModalVisible] = React.useState(false);
  const genders = [
    {label: 'Female', value: 'F_'},
    {label: 'Male', value: 'M_'},
    {label: 'Non-Binary', value: 'NB_'},
  ];
  const [option, setOption] = React.useState(null);
  // age options:
  const [lowerAge, setLowerAge] = React.useState(null);
  const [upperAge, setUpperAge] = React.useState(null);

  const [rerender, setRerender] = React.useState(false); // used to force screen to rerender.
  // default message if user is out of matches.
  const outOfMatches = {
    key: 'empty',
    userId: 'empty',
    name: 'No Matches Found',
    age: '',
    gender: '',
    bio: 'Try re-loading the app or come again later! \nCheck out some of our favorite books in the meantime:',
    favGenre: '',
    books: ['../images/title1.png', '../images/title3.png'],
  };
  const [matches, setMatches] = React.useState([outOfMatches]); // list of matches.

  // loads list of potential matches when entering this screen.
  React.useEffect(() => {
    // calls method from Client.js to get list of matches from database.
    Client.getPotentialMatches().then(data => {
      // sets the default empty message at the front of the array.
      setMatches([outOfMatches]);

      // collects/organizes user info for all potential matches.
      for (let i = 0; i < data.length; i++) {
        let match = data[i];
        // does not add user if they do not have a 'name' or 'userId'.
        if ('name' in match && 'userId' in match) {
          // set default profile parameters.
          let matchInfo = {
            key: i,
            userId: match.userId,
            name: match.name,
            age: 'N/A',
            gender: '',
            bio: match.name + ' does not have a bio yet.',
            favGenre: 'N/A',
            books: ['../images/nobook.png', '../images/nobook.png'],
          };

          // add user profile parameters if possible.
          // adds age.
          if ('birthday' in match) {
            // calculates user age using their birthday.
            let birthYear = parseInt(match.birthday.split('-')[0]);
            if (!isNaN(birthYear)) {
              let currentYear = new Date().getFullYear();
              matchInfo.age = currentYear - birthYear;
            }
          }
          // adds gender.
          if ('gender' in match) {
            matchInfo.gender = match.gender;
          }
          // adds bio.
          if ('bio' in match) {
            matchInfo.bio = match.bio;
          }
          // adds favorite genre.
          if ('favGenre' in match) {
            matchInfo.favGenre = match.favGenre;
          }

          // updates name to solely include first name.
          if (match.name.split(' ').length > 1) {
            matchInfo.name = match.name.split(' ')[0];
          }

          // add new user data to the end of the array.
          setMatches(prevState => {
            prevState.push(matchInfo);
            return [...prevState];
          });
        }
      }
    });
  }, []);

  const updateMatch = () => {
    // if only remaining "profile" is the default message, do not change.
    if (matches[matches.length - 1].key !== 'empty') {
      // otherwise, go to next profile.
      matches.pop();
    }
    setRerender(!rerender); // forces rerender
  };

  // very basic preference input validation.
  const validateInput = () => {
    // ensures that preferences are a valid age range.
    return lowerAge <= upperAge;
  };

  // renders three main sections: topMenu, matchMenu, bottomMenu (also contains preferences menu)
  return (
    <SafeAreaView style={styles.container}>
      <SafeAreaView style={styles.topMenu}>
        <Text style={styles.title}>BookBae</Text>
        <Pressable
          style={styles.button}
          onPress={() => setModalVisible(!modalVisible)}>
          <Text style={styles.buttonText}>Preferences</Text>
        </Pressable>
      </SafeAreaView>
      <SafeAreaView>
        <Modal animationType="slide" visible={modalVisible}>
          <SafeAreaView style={styles.preferencesMenu}>
            <Pressable
              style={styles.button}
              onPress={() => {
                // checks if age preferences are valid.
                if (validateInput()) {
                  // sends input to database.
                  Client.setPreferences({
                    lowerAgeLimit: lowerAge,
                    upperAgeLimit: upperAge,
                    withinXMiles: 100,
                    preferredGender: option,
                  });
                  // closes preferences menu.
                  setModalVisible(!modalVisible);
                }
              }}>
              <Text style={styles.buttonText}>Done</Text>
            </Pressable>
            <Text style={styles.preferencesText}>Select Gender:</Text>
            <RadioForm
              radio_props={genders}
              initial={-1}
              onPress={value => {
                setOption(value);
              }}
            />
            <Text style={styles.preferencesText}>Select Age Range:</Text>
            <SafeAreaView style={{flexDirection: 'row'}}>
              <TextInput
                style={styles.preferencesInput}
                numberOfLines={1}
                onChangeText={setLowerAge}
                value={lowerAge}
                keyboardType="number-pad"
              />
              <Text style={styles.preferencesText}>To</Text>
              <TextInput
                style={styles.preferencesInput}
                numberOfLines={1}
                onChangeText={setUpperAge}
                value={upperAge}
                keyboardType="number-pad"
              />
            </SafeAreaView>
          </SafeAreaView>
        </Modal>
      </SafeAreaView>
      <SafeAreaView style={styles.matchMenu}>
        <ProfileCard profile={matches[matches.length - 1]} />
      </SafeAreaView>
      <SafeAreaView style={styles.bottomMenu}>
        <Pressable
          style={matchStyles.button}
          onPress={() => {
            updateMatch();
          }}>
          <Image
            style={matchStyles.buttonIcon}
            source={require('../images/deny.png')}
          />
        </Pressable>
        <Pressable
          style={matchStyles.button}
          onPress={() => {
            if (matches[matches.length - 1].userId !== 'empty') {
              // Client.sendLike(matches[matches.length - 1].userId);
            }
            updateMatch();
          }}>
          <Image
            style={matchStyles.buttonIcon}
            source={require('../images/accept.png')}
          />
        </Pressable>
      </SafeAreaView>
    </SafeAreaView>
  );
};

export default HomeScreen;

const ProfileCard = ({profile}) => {
  // displays data for a given profile.
  return (
    <SafeAreaView style={matchStyles.matchBox}>
      <SafeAreaView style={matchStyles.header}>
        <Text style={matchStyles.name}>{profile.name}</Text>
        <Text style={matchStyles.age}>
          {profile.age}
          {profile.gender}
        </Text>
      </SafeAreaView>
      <SafeAreaView style={matchStyles.bioContainer}>
        <Text style={matchStyles.bioText}>{profile.bio}</Text>
      </SafeAreaView>
      <SafeAreaView style={matchStyles.bookDisplay}>
        <Image
          style={matchStyles.book}
          source={require('../images/title1.jpg')}
        />
        <Image
          style={matchStyles.book}
          source={require('../images/title3.jpg')}
        />
      </SafeAreaView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
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

const matchStyles = StyleSheet.create({
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
