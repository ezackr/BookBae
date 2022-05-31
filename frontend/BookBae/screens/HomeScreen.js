import React from 'react';
import {StyleSheet, Text, SafeAreaView, Image, Pressable} from 'react-native';
import Client from '../Client.js';

/**
 * Main App screen used to suggest matches to users.
 */
const HomeScreen = ({navigation}) => {
  const [rerender, setRerender] = React.useState(false); // used to force screen to rerender.
  // default message if user is out of matches.
  const outOfMatches = {
    key: 'empty',
    userId: 'empty',
    name: 'No Matches Found',
    age: '',
    gender: '',
    bio: 'Try re-loading the screen or come again later! \nCheck out some of our favorite books:',
    favGenre: '',
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
          };

          // add user profile parameters if possible.
          // adds age.
          if ('birthday' in match) {
            // calculates user age using their birthday.
            let birthYear = parseInt(match.birthday.split('-')[0]);
            if (!isNaN(birthYear)) {
              let currentYear = new Date().getFullYear();
              let age = currentYear - birthYear;
              matchInfo.age = age;
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

  const onMatch = () => {
    // if only remaining "profile" is the default message, do not change.
    if (matches[matches.length - 1].key !== 'empty') {
      // otherwise, go to next profile.
      matches.pop();
    }
    setRerender(!rerender); // forces rerender
  };

  // renders three main sections: topMenu, matchMenu, bottomMenu
  return (
    <SafeAreaView style={styles.container}>
      <SafeAreaView style={styles.topMenu}>
        <Text style={styles.title}>BookBae</Text>
      </SafeAreaView>
      <SafeAreaView style={styles.matchMenu}>
        <ProfileCard profile={matches[matches.length - 1]} />
      </SafeAreaView>
      <SafeAreaView style={styles.bottomMenu}>
        <Pressable
          style={matchStyles.button}
          onPress={() => {
            onMatch();
          }}>
          <Image source={require('../images/deny.png')} />
        </Pressable>
        <Pressable
          style={matchStyles.button}
          onPress={() => {
            onMatch();
          }}>
          <Image source={require('../images/accept.png')} />
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
  container: {
    flex: 1,
    backgroundColor: '#ffe9c1',
    alignItems: 'center',
    justifyContent: 'center',
  },
  topMenu: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'flex-start',
    backgroundColor: '#a53f3f',
    alignSelf: 'stretch',
  },
  matchMenu: {
    flex: 6,
    justifyContent: 'center',
    alignItems: 'center',
  },
  bottomMenu: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    color: 'black',
    fontSize: 48,
    fontWeight: 'bold',
    paddingLeft: 20,
    padding: 10,
  },
});

const matchStyles = StyleSheet.create({
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
  header: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    alignSelf: 'stretch',
    padding: 10,
  },
  name: {
    color: 'black',
    fontSize: 32,
    fontWeight: 'bold',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
  },
  age: {
    color: '#242526',
    fontSize: 24,
  },
  bioContainer: {
    flex: 3,
    alignSelf: 'stretch',
  },
  bioText: {
    fontSize: 24,
    color: 'black',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    alignSelf: 'stretch',
    padding: 10,
  },
  bookDisplay: {
    flex: 3,
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    alignItems: 'flex-end',
    padding: 5,
  },
  book: {
    height: 175,
    width: 125,
    margin: 10,
    borderRadius: 10,
  },
  button: {
    margin: 20,
    paddingBottom: 10,
  },
});
