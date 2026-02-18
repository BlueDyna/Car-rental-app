# Naming convention

1.  ### Main component (header, page, footer)

    > e.g. `"header."`

2.  ### Sub-component

    > e.g. `"header.nav"`

3.  ### Entry

    > e.g. `"header.nav.cars": "Cars"`

4.  ### Sub-entries
    > if entry has multiple items (e.g. dropdown)
    ```
    "header.nav": {
        "cars": "Cars",
        "rentals": "Rentals",
        "rents": "Rents"
    }
    ```

## Result

```
{
...

    "header.nav":{

        "cars": "Cars"
        "rentals": "Rentals",
        "rents": "Rents"

    },
...
}
```
