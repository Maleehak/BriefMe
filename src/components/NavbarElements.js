import { NavLink as Link } from "react-router-dom";
import styled from "styled-components";

export const Nav = styled.nav`
    background: #451059;
    height: 85px;
    display: flex;
    justify-content: center;
    padding: 10px;
    z-index: 15;
`;

export const NavLink = styled(Link)`
    color: #f7f5f7;
    display: flex;
    align-items: center;
    text-decoration: none;
    padding: 0 1rem;
    height: 100%;
    cursor: pointer;
    font-size: 18px;
    &.active {
        color: #edb0f5;
        font-size: 20px;
    }
`;

export const NavMenu = styled.div`
    display: flex;
    align-items: center;
`;


