import React from 'react';
import {
  StyleSheet,
  Text,
  SafeAreaView,
  Pressable,
  Image,
  TouchableOpacity,
} from 'react-native';
import Client from '../Client.js';

const HomeScreen = ({navigation}) => {
  const [matches, setMatches] = React.useState([]);

  const getMatchData = async () => {
    if (matches.length === 0) {
      Client.getPotentialMatches().then(data => {
        for (let i = 0; i < data.length; i++) {
          let match = data[i];
          if ('name' in match) {
            setMatches(prevState => {
              prevState.push({
                key: i,
                name: match.name,
              });
              return [...prevState];
            });
          }
        }
        console.log('matches: ' + matches);
      });
    }
  };

  React.useEffect(() => {
    getMatchData();
  });

  return (
    <SafeAreaView style={styles.container}>
      <SafeAreaView style={styles.topMenu}>
        <Text style={styles.title}>BookBae</Text>
      </SafeAreaView>
      <SafeAreaView style={styles.matchMenu}>
        {matches.map(match => (
          <Text key={match.key} style={styles.bodyText}>{match.name}</Text>
        ))}
      </SafeAreaView>
      <SafeAreaView style={styles.bottomMenu}>
        <TouchableOpacity
          style={styles.button}
          activeOpacity={0.5}
          onPress={getMatchData}>
          <Image source={require('../images/deny.png')} />
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          activeOpacity={0.5}
          onPress={getMatchData}>
          <Image source={require('../images/accept.png')} />
        </TouchableOpacity>
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
      <Text style={matchStyles.frontName}>Alan, 29</Text>
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
    paddingBottom: 20,
  },
  title: {
    color: 'black',
    fontSize: 48,
    fontWeight: 'bold',
    padding: 10,
  },
  bodyText: {
    color: 'black',
    fontSize: 18,
  },
  button: {
    height: 50,
    margin: 10,
  },
});

const matchStyles = StyleSheet.create({
  matchBox: {
    height: 500,
    width: 300,
    backgroundColor: '#ffe9a1',
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
});
