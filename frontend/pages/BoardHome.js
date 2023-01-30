import { NavLink, Outlet } from 'react-router-dom';

const BoardHome = () => {
  return (
    <div>
      <Outlet />
      <ul>
        <BoardItem id={1} />
        <BoardItem id={2} />
        <BoardItem id={3} />
      </ul>
    </div>
  );
};

const BoardItem = ({ id }) => {
  const activeStyle = {
    color: 'green',
    fontSize: 21,
  };
  return (
    <li>
      <NavLink
        to={`/board/${id}`}
        style={({ isActive }) => (isActive ? activeStyle : undefined)}
      >
        게시글 {id}
      </NavLink>
    </li>
  );
};

export default BoardHome;