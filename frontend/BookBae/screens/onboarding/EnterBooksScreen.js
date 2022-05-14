import React from 'react';
import {
  SafeAreaView,
  View,
  StyleSheet,
  Text,
  TextInput,
  Pressable,
  Image,
  FlatList,
} from 'react-native';
import axios from 'axios';

const EnterBooksScreen = ({route, navigation}) => {
  const bookCover =
    'https://www.pngitem.com/pimgs/m/19-191625_icon-plus-png-gray-plus-icon-png-transparent.png';

  const [text, setText] = React.useState(null);
  const [bookList, setBookList] = React.useState([]);

  // we probably don't need to do anything here, we can store books as they are added in onChangeText
  const onPress = () => {
    console.log(bookCover);
    navigation.navigate('EnterBioScreen', {
        email: route.params.email,
        password: route.params.password,
        name: route.params.name,
        gender: route.params.gender,
        birthday: route.params.birthday,
        zipcode: route.params.zipcode,
        genre: route.params.genre,
        books: bookList
    });
  };

  const addNewBook = () => {
    axios
      .get('https://www.googleapis.com/books/v1/volumes?q=' + text)
      .then(data => {
        let book = data.data.items[0];
        // add new book to stored list.
        setBookList(prevState => {
          prevState.push({
            title: book.volumeInfo.title,
          });
          return [...prevState];
        });
        console.log(bookList);
      });
    setText('');
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Start Typing a Book Title:</Text>
      <TextInput
        style={styles.input}
        placeholder="Insert Book Title"
        onChangeText={setText}
        onSubmitEditing={() => addNewBook()}
        defaultValue={text}
      />
      <View style={styles.bookList}>
        <FlatList
          data={bookList}
          extraData={bookList}
          renderItem={({item}) => <Text style={styles.book}>{item.title}</Text>}
        />
      </View>
      <View style={styles.imageContainer}>
        <Image style={styles.image} source={{uri: bookCover}} />
      </View>
      <Pressable style={styles.button} onPress={onPress}>
        <Text style={styles.buttonText}>Next</Text>
      </Pressable>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
  bookList: {
    flex: 1,
    padding: 10,
  },
  book: {
    color: 'black',
    fontSize: 24,
  },
  input: {
    height: 40,
    width: '70%',
    margin: 1,
    borderWidth: 1,
    padding: 10,
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  imageContainer: {
    paddingTop: 5,
    paddingBottom: 5,
  },
  image: {
    width: 200,
    height: 200,
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: '#BD2A2A',
    marginTop: 5,
    marginBottom: 5,
  },
  buttonText: {
    fontSize: 16,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white',
  },
});

export default EnterBooksScreen;
