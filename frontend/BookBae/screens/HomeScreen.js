import React from 'react';
import {
  Image,
  Pressable,
  SafeAreaView,
  Text,
  Modal,
  TextInput,
  FlatList,
} from 'react-native';
import RadioForm from 'react-native-simple-radio-button';
import Client from '../Client.js';
import {styles, matchStyles} from './HomeScreenStyles';

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
    books: [
      // default cover links for "Design Patterns" and "The Anthropocene Reviewed"
      'http://books.google.com/books/content?id=tmNNfSkfTlcC&printsec=frontcover&img=1&zoom=5&edge=curl&imgtk=AFLRE71W9IUGyrL1thP-LPEsEi3H6vvCOF5Um7wV8xg8969MQeFRA7taYchYCh6tgtu2RczvrSoDYoAX6Dlm5-sNHpq2tzJoBmJi8P3Zs7KFV03-1HVFHqp7DsgHfmxS2_6HZRCgbg6A&source=gbs_api',
      'http://books.google.com/books/publisher/content?id=ZPD4DwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&imgtk=AFLRE73rvbRxqCCY7Oddo4zG0vGaY_SmuiM3_hen2TBKRa_aqMem38LpZIqZ5VWggsE9_yFLuGkEd8g5BsfWbMhffsoR6Wgj7mcnv2A7M7ZufvSt-aec1rvorwAOxtIxUPtFH-SLigKI&source=gbs_api',
    ],
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
          // adds books.
          if ('bookList' in match) {
            let books = [];
            if (match.bookList.entries.length >= 2) {
              for (let j = 0; j < 2; j++) {
                books[j] = match.bookList.entries[j].bookId;
              }
            }
            matchInfo.books = books;
          }

          // updates name to solely include first name.
          if (match.name.split(' ').length > 1) {
            matchInfo.name = match.name.split(' ')[0];
          }

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
  const setPreferences = () => {
    // ensures that preferences are a valid age range.
    let lower = parseInt(lowerAge, 10);
    let upper = parseInt(upperAge, 10);
    if (!isNaN(lower) && !isNaN(upper) && lower <= upper) {
      Client.setPreferences({
        lowerAgeLimit: lowerAge,
        upperAgeLimit: upperAge,
        withinXMiles: 100,
        preferredGender: option,
      });
    }
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
                // sends input to database.
                setPreferences();
                // closes preferences menu.
                setModalVisible(!modalVisible);
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
              <Text style={styles.preferencesText}>to</Text>
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

/**
 * Displays all relevant profile information for a potential match.
 */
const ProfileCard = ({profile}) => {
  // track books as they load in. Default to 'nobook.png'
  const [bookList, setBookList] = React.useState([]);

  React.useEffect(() => {
    if (profile) {
      setBookList(profile.books);
    }
  }, [profile]);

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
        <FlatList
          horizontal={true}
          data={bookList}
          extraData={bookList}
          renderItem={({item}) => <BookItem book={item} />}
        />
      </SafeAreaView>
    </SafeAreaView>
  );
};

/**
 * BookItem to be put in display.
 */
const BookItem = ({book}) => {
  return (
    <SafeAreaView>
      <Image
        style={matchStyles.book}
        source={{uri: book}}
      />
    </SafeAreaView>
  );
};
