import React from 'react';
import {
  StyleSheet,
  Text,
  SafeAreaView,
  Image,
  Pressable,
} from 'react-native';
import Client from '../Client.js';

const HomeScreen = ({navigation}) => {
  const [rerender, setRerender] = React.useState(false);
  const outOfMatches = {key: 'empty', name: 'no more matches'};
  const [matches, setMatches] = React.useState([outOfMatches]);

  React.useEffect(() => {
    Client.getPotentialMatches().then(data => {
      setMatches([outOfMatches]);
      for (let i = 0; i < data.length; i++) {
        let match = data[i];
        if ('name' in match) {
          setMatches(prevState => {
            prevState.push({
              key: i + 1,
              name: match.name,
            });
            return [...prevState];
          });
        }
      }
    });
  }, []);

  const onMatch = () => {
    console.log('before: ' + matches[matches.length - 1].name);
    if (matches[matches.length - 1].key !== 'empty') {
      matches.pop();
    }
    setRerender(!rerender);
    console.log('after: ' + matches[matches.length - 1].name);
  };

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
  return (
    <SafeAreaView style={matchStyles.matchBox}>
      <SafeAreaView style={matchStyles.bookDisplay}>
        <Image
          style={matchStyles.book}
          source={require('../images/title1.jpg')}
        />
        <Image
          style={matchStyles.book}
          source={require('../images/title2.jpeg')}
        />
      </SafeAreaView>
      <Text style={matchStyles.frontName}>{profile.name}</Text>
      <SafeAreaView style={matchStyles.bookDisplay}>
        <Image
          style={matchStyles.book}
          source={require('../images/title3.jpg')}
        />
        <Image
          style={matchStyles.book}
          source={require('../images/title4.jpeg')}
        />
      </SafeAreaView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
  },
  topMenu: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
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
    padding: 10,
  },
});

const matchStyles = StyleSheet.create({
  matchBox: {
    height: 475,
    width: 300,
    backgroundColor: 'white',
    borderRadius: 25,
    justifyContent: 'center',
    alignItems: 'center',
  },
  frontName: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
    justifyContent: 'center',
    alignItems: 'center',
  },
  bookDisplay: {
    flexDirection: 'row',
    justifyContent: 'space-evenly',
    alignItems: 'center',
  },
  book: {
    height: 175,
    width: 125,
    margin: 10,
  },
  button: {
    margin: 20,
    paddingBottom: 10,
  },
});
