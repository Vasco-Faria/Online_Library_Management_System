import './App.css';
import Navbar from './components/navbar';
import Homepage from './components/homepage';
import ReservationsPage from './components/reservations';
import Homelog from './components/homelog';
import Register from './components/register';
import Login from './components/login';
import UserHome from './components/userHome';
import '@fortawesome/fontawesome-free/css/all.css';
import { Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import BooksSearch from './components/BooksSearch';
import Layout from './context/Layout';
import BookDetailPage from './components/BookDetailPage';
import PrivateRoute from './context/PrivateRoute';
import ReservationPage from './components/reservationPage';
import Profile from './components/profile';
import NotificationsPage from './components/notificationspage';
import Home from './components/Home';
import MyReserves from './components/MyReserves';
import Favorites from './components/Favorites';
import EbookReader from './components/EbookReader';
import ReservationDetailPage from './components/ReservationDetailPage';
import Statistics from './components/Statistics';


function App() {
  return (
    <AuthProvider>
      <Navbar />
      <Layout />
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path="/homepage" element={<Homepage />} />
        <Route path="/homelog" element={<Homelog />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/booksearch" element={<BooksSearch />} />
        <Route path="/book/:id" element={<BookDetailPage />} />
        <Route path="/ReservationDetailPage/:reservationId" element={<ReservationDetailPage />} />
        <Route path="/book/:id" element={<BookDetailPage/>} />
        <Route path="/statistics" element={<Statistics/>}/>
        <Route path="/bibliotecario" element={
          <PrivateRoute>
            <Homepage />
          </PrivateRoute>
        } />
        <Route path="/notifications" element={
          <PrivateRoute>
            <NotificationsPage />
          </PrivateRoute>
        } />
        <Route path="/reservations" element={
          <PrivateRoute>
            <ReservationsPage />
          </PrivateRoute>
        } />
        <Route path="/reservationPage" element={
          <PrivateRoute>
            <ReservationPage />
          </PrivateRoute>
        } />
        <Route path="/userhome" element={
          <PrivateRoute>
            <UserHome />
          </PrivateRoute>
        } />
        <Route path="/profile" element={
          <PrivateRoute>
            <Profile />
          </PrivateRoute>
        } />

        <Route path="/favorites" element={
          <PrivateRoute>
            <Favorites />
          </PrivateRoute>
        } />

        <Route path="/myreserves" element={
          <PrivateRoute>
            <MyReserves />
          </PrivateRoute>
        } />
        <Route path="/myebook" element={
          <PrivateRoute>
            <EbookReader />
          </PrivateRoute>
        } />
      </Routes>
    </AuthProvider>
  );
}

export default App;
